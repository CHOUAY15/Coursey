package com.ensa.projet.participantservice.controller;


import com.ensa.projet.participantservice.dto.EnrollmentDto;

import com.ensa.projet.participantservice.service.interfaces.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    public ResponseEntity<?> enrollInTraining(
            @RequestParam Integer participantId,
            @RequestParam Integer trainingId) {
        try {
            return ResponseEntity.ok(enrollmentService.enrollInTraining(participantId, trainingId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Participant not found: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Enrollment failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }
    @GetMapping("/{enrollmentId}")
    public ResponseEntity<EnrollmentDto> getEnrollmentById(@PathVariable Integer enrollmentId) {
        try {
            EnrollmentDto enrollmentDto = enrollmentService.findEnrollemntById(enrollmentId);
            return ResponseEntity.ok(enrollmentDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }


    @PutMapping("/{enrollmentId}/modules/{moduleId}/complete")
    public ResponseEntity<?> markModuleComplete(
            @PathVariable Integer enrollmentId,
            @PathVariable Integer moduleId) {
        try {
            enrollmentService.markModuleComplete(enrollmentId, moduleId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/check")
    public ResponseEntity<EnrollmentDto> checkEnrollment(
            @RequestParam Integer participantId,
            @RequestParam Integer trainingId) {
        return ResponseEntity.ok(enrollmentService.getEnrollment(participantId, trainingId));
    }

    @GetMapping("/participant/{participantId}")
    public ResponseEntity<?> getParticipantEnrollments(@PathVariable Integer participantId) {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getParticipantEnrollments(participantId);
            if (enrollments.isEmpty()) {
                return ResponseEntity.ok().body("No enrollments found for participant: " + participantId);
            }
            return ResponseEntity.ok(enrollments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

}