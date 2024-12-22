package com.ensa.projet.participantservice.repository;

import com.ensa.projet.participantservice.entities.ContentProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentProgressRepository extends JpaRepository<ContentProgress, Integer> {
    Optional<ContentProgress> findByEnrollmentIdAndContentId(Integer enrollmentId, Integer contentId);
}