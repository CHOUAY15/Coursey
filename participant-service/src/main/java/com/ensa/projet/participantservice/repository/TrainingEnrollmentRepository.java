package com.ensa.projet.participantservice.repository;

import com.ensa.projet.participantservice.entities.TrainingEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainingEnrollmentRepository extends JpaRepository<TrainingEnrollment, Integer> {
    List<TrainingEnrollment> findByParticipantId(Integer participantId);
    Optional<TrainingEnrollment> findByParticipantIdAndTrainingId(Integer participantId, Integer trainingId);
}