package com.ensa.projet.participantservice.service.interfaces;

import com.ensa.projet.participantservice.dto.EnrollmentDTO;
import com.ensa.projet.participantservice.dto.ModuleProgressDTO;
import java.util.List;

public interface EnrollmentService {
    EnrollmentDTO enrollInTraining(Integer participantId, Integer trainingId);
    EnrollmentDTO getEnrollment(Integer enrollmentId);
    List<EnrollmentDTO> getParticipantEnrollments(Integer participantId);
    void updateModuleProgress(Integer enrollmentId, Integer moduleId, ModuleProgressDTO progress);
    void markContentComplete(Integer enrollmentId, Integer contentId);
    void markModuleComplete(Integer enrollmentId, Integer moduleId);
    void cancelEnrollment(Integer enrollmentId);
    boolean isEnrolled(Integer participantId, Integer trainingId);
}