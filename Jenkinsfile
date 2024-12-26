pipeline {
    agent any

    environment {
        BRANCH_DEV = 'origin/develop'
        BRANCH_PROD = 'origin/main'
        NEXUS_PROXY = "http://192.168.11.137:9082"
        NEXUS_PRIVATE = "http://192.168.11.137:9083"
        NEXUS_CREDENTIALS_ID = "nexus-credentials"
        SERVICES = "config-service,discovery-service,gateway-service,participant-service,training-service"
        TEST_SERVER = '192.168.11.138'
        TEST_SERVER_USER = 'chouay'
        PROJECT_PATH = '/home/chouay/first-aid'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    def changedFiles = sh(script: "git diff --name-only HEAD~1 HEAD", returnStdout: true).trim()
                    env.MODIFIED_SERVICES = SERVICES.split(',').findAll { service ->
                        changedFiles.contains(service)
                    }.join(',')
                    echo "Modified services: ${env.MODIFIED_SERVICES}"
                }
            }
        }

        stage('Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Sonarqube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('sonar-server') {
                        sh "mvn sonar:sonar -Dintegration-tests.skip=true -Dmaven.test.failure.ignore=true"
                    }
                    timeout(time: 1, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                }
            }
        }

        stage('Maven Build and Package') {
            steps {
                script {
                    sh 'mvn clean package -DskipTests'
                }
            }
            post {
                success {
                    script {
                        // Archive artifacts from each service's target directory
                        SERVICES.split(',').each { service ->
                            archiveArtifacts artifacts: "${service}/target/*.jar", allowEmptyArchive: true
                        }
                    }
                }
                failure {
                    echo 'Maven build failed'
                }
            }
        }

        stage('Docker Build and Push to Nexus') {
            steps {
                script {
                    def envName = env.GIT_BRANCH == BRANCH_PROD ? "prod" : "dev"
                    def modifiedServicesList = env.MODIFIED_SERVICES ? env.MODIFIED_SERVICES.split(',') : []

                    modifiedServicesList.each { service ->
                        echo "Processing service: ${service}"
                        def version = getEnvVersion(service, envName)
                        echo "Building Docker image for ${service} with version ${version}"

                        dir(service) {
                            echo "Service directory contents:"
                            sh 'ls -la'

                            withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIALS_ID}",
                                    usernameVariable: 'USER',
                                    passwordVariable: 'PASSWORD')]) {
                                sh """
                                    echo \$PASSWORD | docker login -u \$USER --password-stdin ${NEXUS_PRIVATE}
                                    docker build -t ${NEXUS_PRIVATE}/${service}:${version} .
                                    docker push ${NEXUS_PRIVATE}/${service}:${version}
                                """
                            }
                        }
                    }
                }
            }
        }

        stage('Deploy to Test Server') {
            steps {
                script {
                    if (!env.MODIFIED_SERVICES) {
                        echo "No services were modified. Skipping deployment."
                        return
                    }

                    sshagent(credentials: ['ssh-credentials-id']) {
                        def envName = env.GIT_BRANCH == BRANCH_PROD ? "prod" : "test"
                        def modifiedServicesList = env.MODIFIED_SERVICES.split(',')

                        withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIALS_ID}",
                                usernameVariable: 'NEXUS_USERNAME',
                                passwordVariable: 'NEXUS_PASSWORD')]) {

                            // Login to Nexus on test server
                            sh """
                                echo \$NEXUS_PASSWORD | ssh -o StrictHostKeyChecking=no ${TEST_SERVER_USER}@${TEST_SERVER} \
                                'docker login ${NEXUS_PRIVATE} --username "\${NEXUS_USERNAME}" --password-stdin'
                            """

                            modifiedServicesList.each { service ->
                                def version = getEnvVersion(service, envName)

                                sh """
                                    ssh -o StrictHostKeyChecking=no ${TEST_SERVER_USER}@${TEST_SERVER} '
                                        set -ex
                                        cd ${PROJECT_PATH}
                                        
                                        # Update .env file
                                        echo "NEXUS_PRIVATE=${NEXUS_PRIVATE}" > .env
                                        echo "VERSION=${version}" >> .env
                                        
                                        # Pull and update service
                                        docker-compose pull ${service}
                                        docker-compose up -d --no-deps ${service}
                                        
                                        # Wait for service to be healthy
                                        TIMEOUT=300
                                        while [ \$TIMEOUT -gt 0 ]; do
                                            if docker-compose ps ${service} | grep -q "Up"; then
                                                echo "${service} is up and running"
                                                break
                                            fi
                                            sleep 5
                                            TIMEOUT=\$((TIMEOUT-5))
                                        done
                                        
                                        if [ \$TIMEOUT -le 0 ]; then
                                            echo "${service} failed to start within timeout"
                                            docker-compose logs ${service}
                                            exit 1
                                        fi
                                    '
                                """
                            }
                        }
                    }
                }
            }
        }
    }
}

def getEnvVersion(service, envName) {
    // Don't use dir() inside dir() - this was causing the path issue
    def pom = readMavenPom file: "${service}/pom.xml"
    def artifactVersion = pom.version
    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()

    def versionNumber = gitCommit ?
            "${artifactVersion}-${envName}.${env.BUILD_NUMBER}.${gitCommit.take(8)}" :
            "${artifactVersion}-${envName}.${env.BUILD_NUMBER}"

    echo "Build version for service ${service}: ${versionNumber}"
    return versionNumber
}