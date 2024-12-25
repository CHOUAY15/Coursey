package com.ensa.projet.participantservice.service.imple;

import com.ensa.projet.participantservice.client.TrainingServiceClient;
import com.ensa.projet.participantservice.dto.ParticipantAnswerDTO;
import com.ensa.projet.participantservice.dto.QuizDTO;
import com.ensa.projet.participantservice.dto.TestResultSubmissionDTO;
import com.ensa.projet.participantservice.dto.TrainingDTO;
import com.ensa.projet.participantservice.entities.ParticipantAnswer;
import com.ensa.projet.participantservice.entities.TestResult;
import com.ensa.projet.participantservice.repository.TestResultRepository;
import com.ensa.projet.participantservice.service.interfaces.TestResultService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TestResultServiceImpl implements TestResultService {



    private final TestResultRepository testResultRepository;

    private final TrainingServiceClient trainingServiceClient;

    public TestResultServiceImpl(TestResultRepository testResultRepository, TrainingServiceClient trainingServiceClient) {
        this.testResultRepository = testResultRepository;
        this.trainingServiceClient = trainingServiceClient;
    }

    @Override
    public TestResult submitTestResult(TestResultSubmissionDTO submission) {
        // Get quizzes from training service
        List<QuizDTO> quizzes = trainingServiceClient.getQuizzesByModuleId(submission.getTrainingId());

        // Calculate score using quizzes from service
        float score = calculateScore(submission.getAnswers(), quizzes);
        boolean passed = score >= 70.0f;

        // Create test result
        TestResult testResult = TestResult.builder()
                .participantId(submission.getParticipantId())
                .trainingId(submission.getTrainingId())
                .score(score)
                .submissionDate(LocalDateTime.now())
                .passed(passed)
                .build();

        // Create participant answers
        List<ParticipantAnswer> participantAnswers = submission.getAnswers().stream()
                .map(answerDTO -> ParticipantAnswer.builder()
                        .quizId(answerDTO.getQuizId())
                        .selectedAnswerIndex(answerDTO.getSelectedAnswerIndex())
                        .build())
                .toList(); // Produces an unmodifiable list in Java 16+


        testResult.setUserAnswers(participantAnswers);

        return testResultRepository.save(testResult);
    }

    private float calculateScore(List<ParticipantAnswerDTO> answers, List<QuizDTO> quizzes) {
        int correctAnswers = 0;

        // Create a map of quiz IDs to correct answers for efficient lookup
        Map<Integer, Integer> quizCorrectAnswers = quizzes.stream()
                .collect(Collectors.toMap(QuizDTO::getId, QuizDTO::getCorrectAnswerIndex));

        for (ParticipantAnswerDTO answer : answers) {
            Integer correctIndex = quizCorrectAnswers.get(answer.getQuizId());
            if (correctIndex != null && Objects.equals(correctIndex, answer.getSelectedAnswerIndex())) {
                correctAnswers++;
            }
        }

        return (correctAnswers * 100.0f) / answers.size();
    }

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