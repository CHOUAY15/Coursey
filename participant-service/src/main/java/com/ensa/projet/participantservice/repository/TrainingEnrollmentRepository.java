package com.ensa.projet.participantservice.repository;

import com.ensa.projet.participantservice.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainingEnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    Enrollment findByParticipantIdAndTrainingId(Integer participantId, Integer trainingId);
}