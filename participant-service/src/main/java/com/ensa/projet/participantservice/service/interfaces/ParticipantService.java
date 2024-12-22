package com.ensa.projet.participantservice.service.interfaces;

import com.ensa.projet.participantservice.dto.CertificationDTO;
import com.ensa.projet.participantservice.dto.KeycloakUserInfo;
import com.ensa.projet.participantservice.dto.ParticipantDTO;
import com.ensa.projet.participantservice.dto.LearningStatsDTO;

import java.util.List;
import java.util.Optional;

public interface ParticipantService {
    ParticipantDTO createParticipant(String userId, KeycloakUserInfo userInfo);
    ParticipantDTO updateParticipant(Integer id, ParticipantDTO participantDTO);
    Optional<ParticipantDTO> getParticipantByUserId(String userId);
    List<CertificationDTO> getParticipantCertifications(Integer participantId);
    void deleteParticipant(Integer id);
    LearningStatsDTO getParticipantLearningStats(Integer participantId);
}