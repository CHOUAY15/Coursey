package com.ensa.projet.participantservice.controller;

import com.ensa.projet.participantservice.dto.*;
import com.ensa.projet.participantservice.service.interfaces.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    @PostMapping
    public ResponseEntity<ParticipantDTO> createParticipant(
            @RequestParam String userId,
            @RequestBody KeycloakUserInfo userInfo) {
        return ResponseEntity.ok(participantService.createParticipant(userId, userInfo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParticipantDTO> updateParticipant(
            @PathVariable Integer id,
            @Valid @RequestBody ParticipantDTO participantDTO) {
        ParticipantDTO updated = participantService.updateParticipant(id, participantDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ParticipantDTO> getParticipantByUserId(@PathVariable String userId) {
        return participantService.getParticipantByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/certifications")
    public ResponseEntity<List<CertificationDTO>> getParticipantCertifications(@PathVariable Integer id) {
        return ResponseEntity.ok(participantService.getParticipantCertifications(id));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<LearningStatsDTO> getParticipantStats(@PathVariable Integer id) {
        LearningStatsDTO stats = participantService.getParticipantLearningStats(id);
        return stats != null ? ResponseEntity.ok(stats) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Integer id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.ok().build();
    }
}
