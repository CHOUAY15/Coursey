package com.ensa.projet.participantservice.repository;

import com.ensa.projet.participantservice.entities.ModuleProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleProgressRepository extends JpaRepository<ModuleProgress, Integer> {
    Optional<ModuleProgress> findByEnrollmentIdAndModuleId(Integer enrollmentId, Integer moduleId);
}