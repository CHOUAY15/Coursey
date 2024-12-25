package com.ensa.projet.participantservice.service.imple;

import com.ensa.projet.participantservice.client.TrainingServiceClient;
import com.ensa.projet.participantservice.dto.TrainingDTO;
import com.ensa.projet.participantservice.entities.TestResult;
import com.ensa.projet.participantservice.repository.TestResultRepository;
import com.ensa.projet.participantservice.service.interfaces.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestResultServiceImpl implements TestResultService {
    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private TrainingServiceClient trainingServiceClient;

    public List<TestResult> getTestResultsByParticipantId(Integer participantId) {
        List<TestResult> testResults = testResultRepository.findByParticipantId(participantId);

        for (TestResult testResult : testResults) {
            // Récupère les données de formation à partir du microservice de formation en utilisant Feign Client
            TrainingDTO trainingDTO = trainingServiceClient.getTraining(testResult.getTrainingId());

            // Affecte directement TrainingDTO au TestResult
            testResult.setTrainingDTO(trainingDTO);
        }

        return testResults;
    }
}