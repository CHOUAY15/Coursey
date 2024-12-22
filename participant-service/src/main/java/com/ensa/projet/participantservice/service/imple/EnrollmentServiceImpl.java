package com.ensa.projet.participantservice.service.imple;

import com.ensa.projet.participantservice.dto.EnrollmentDTO;
import com.ensa.projet.participantservice.dto.ModuleProgressDTO;
import com.ensa.projet.participantservice.entities.*;
import com.ensa.projet.participantservice.repository.*;
import com.ensa.projet.participantservice.service.interfaces.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private TrainingEnrollmentRepository enrollmentRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ModuleProgressRepository moduleProgressRepository;

    @Autowired
    private ContentProgressRepository contentProgressRepository;

    @Override
    public EnrollmentDTO enrollInTraining(Integer participantId, Integer trainingId) {
        if (isEnrolled(participantId, trainingId)) {
            return null;
        }

        var participant = participantRepository.findById(participantId).orElse(null);
        if (participant == null) {
            return null;
        }

        TrainingEnrollment enrollment = TrainingEnrollment.builder()
                .participant(participant)
                .trainingId(trainingId)
                .enrollmentDate(LocalDateTime.now())
                .status(EnrollmentStatus.ENROLLED)
                .overallProgress(0.0f)
                .build();

        enrollment = enrollmentRepository.save(enrollment);
        return convertToDTO(enrollment);
    }

    @Override
    public EnrollmentDTO getEnrollment(Integer enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<EnrollmentDTO> getParticipantEnrollments(Integer participantId) {
        return enrollmentRepository.findByParticipantId(participantId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateModuleProgress(Integer enrollmentId, Integer moduleId, ModuleProgressDTO progress) {
        var moduleProgress = moduleProgressRepository.findByEnrollmentIdAndModuleId(enrollmentId, moduleId)
                .orElse(new ModuleProgress());

        var enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment == null) return;

        moduleProgress.setEnrollment(enrollment);
        moduleProgress.setModuleId(moduleId);
        moduleProgress.setProgressPercentage(progress.getProgressPercentage());
        moduleProgress.setLastAccessed(LocalDateTime.now());
        moduleProgress.setCompleted(progress.isCompleted());

        if (progress.isCompleted() && moduleProgress.getCompletionDate() == null) {
            moduleProgress.setCompletionDate(LocalDateTime.now());
        }

        moduleProgressRepository.save(moduleProgress);
        updateEnrollmentProgress(enrollmentId);
    }

    @Override
    public void markContentComplete(Integer enrollmentId, Integer contentId) {
        var contentProgress = contentProgressRepository.findByEnrollmentIdAndContentId(enrollmentId, contentId)
                .orElse(new ContentProgress());

        var enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment == null) return;

        contentProgress.setEnrollment(enrollment);
        contentProgress.setContentId(contentId);
        contentProgress.setCompleted(true);
        contentProgress.setCompletionDate(LocalDateTime.now());

        contentProgressRepository.save(contentProgress);
        updateEnrollmentProgress(enrollmentId);
    }

    @Override
    public void markModuleComplete(Integer enrollmentId, Integer moduleId) {
        var moduleProgress = moduleProgressRepository.findByEnrollmentIdAndModuleId(enrollmentId, moduleId)
                .orElse(new ModuleProgress());

        var enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment == null) return;

        moduleProgress.setEnrollment(enrollment);
        moduleProgress.setModuleId(moduleId);
        moduleProgress.setProgressPercentage(100f);
        moduleProgress.setCompleted(true);
        moduleProgress.setCompletionDate(LocalDateTime.now());

        moduleProgressRepository.save(moduleProgress);
        updateEnrollmentProgress(enrollmentId);
    }

    @Override
    public void cancelEnrollment(Integer enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
    }

    @Override
    public boolean isEnrolled(Integer participantId, Integer trainingId) {
        return enrollmentRepository.findByParticipantIdAndTrainingId(participantId, trainingId).isPresent();
    }

    private void updateEnrollmentProgress(Integer enrollmentId) {
        var enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment == null) return;

        float totalModules = enrollment.getModuleProgresses().size();
        float completedModules = enrollment.getModuleProgresses().stream()
                .filter(ModuleProgress::isCompleted)
                .count();

        float progress = totalModules > 0 ? (completedModules / totalModules) * 100 : 0;
        enrollment.setOverallProgress(progress);
        enrollment.setLastAccessed(LocalDateTime.now());

        if (progress >= 100) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
            enrollment.setCompletionDate(LocalDateTime.now());
        } else if (progress > 0) {
            enrollment.setStatus(EnrollmentStatus.IN_PROGRESS);
        }

        enrollmentRepository.save(enrollment);
    }

    private EnrollmentDTO convertToDTO(TrainingEnrollment enrollment) {
        return EnrollmentDTO.builder()
                .id(enrollment.getId())
                .participantId(enrollment.getParticipant().getId())
                .trainingId(enrollment.getTrainingId())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .lastAccessed(enrollment.getLastAccessed())
                .completionDate(enrollment.getCompletionDate())
                .status(enrollment.getStatus())
                .overallProgress(enrollment.getOverallProgress())
                .build();
    }
}