package com.ensa.projet.participantservice.controller;

import com.ensa.projet.participantservice.dto.KeycloakUserInfo;
import com.ensa.projet.participantservice.dto.ParticipantDTO;
import com.ensa.projet.participantservice.entities.Participant;
import com.ensa.projet.participantservice.service.interfaces.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;



    @PostMapping
    public ResponseEntity<?> createParticipant(
            @RequestParam String userId,
            @RequestBody KeycloakUserInfo userInfo) {
        try {
            Participant participant = participantService.createParticipant(userId, userInfo);
            return ResponseEntity.ok(participant);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ParticipantDTO> getParticipantByUserId(@PathVariable String userId) {
        ParticipantDTO participant = participantService.getParticipantByUserId(userId);
        if (participant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(participant);
    }


}
