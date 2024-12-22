package com.ensa.projet.participantservice.controller;

import com.ensa.projet.participantservice.dto.EnrollmentDTO;
import com.ensa.projet.participantservice.dto.ModuleProgressDTO;
import com.ensa.projet.participantservice.service.interfaces.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentDTO> enrollInTraining(
            @RequestParam Integer participantId,
            @RequestParam Integer trainingId) {
        EnrollmentDTO enrollment = enrollmentService.enrollInTraining(participantId, trainingId);
        return enrollment != null ? ResponseEntity.ok(enrollment) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getEnrollment(@PathVariable Integer id) {
        EnrollmentDTO enrollment = enrollmentService.getEnrollment(id);
        return enrollment != null ? ResponseEntity.ok(enrollment) : ResponseEntity.notFound().build();
    }

    @GetMapping("/participant/{participantId}")
    public ResponseEntity<List<EnrollmentDTO>> getParticipantEnrollments(@PathVariable Integer participantId) {
        return ResponseEntity.ok(enrollmentService.getParticipantEnrollments(participantId));
    }

    @PutMapping("/{enrollmentId}/modules/{moduleId}/progress")
    public ResponseEntity<Void> updateModuleProgress(
            @PathVariable Integer enrollmentId,
            @PathVariable Integer moduleId,
            @Valid @RequestBody ModuleProgressDTO progress) {
        enrollmentService.updateModuleProgress(enrollmentId, moduleId, progress);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{enrollmentId}/content/{contentId}/complete")
    public ResponseEntity<Void> markContentComplete(
            @PathVariable Integer enrollmentId,
            @PathVariable Integer contentId) {
        enrollmentService.markContentComplete(enrollmentId, contentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{enrollmentId}/modules/{moduleId}/complete")
    public ResponseEntity<Void> markModuleComplete(
            @PathVariable Integer enrollmentId,
            @PathVariable Integer moduleId) {
        enrollmentService.markModuleComplete(enrollmentId, moduleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Void> cancelEnrollment(@PathVariable Integer enrollmentId) {
        enrollmentService.cancelEnrollment(enrollmentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkEnrollment(
            @RequestParam Integer participantId,
            @RequestParam Integer trainingId) {
        return ResponseEntity.ok(enrollmentService.isEnrolled(participantId, trainingId));
    }
}