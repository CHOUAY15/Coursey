package com.ensa.projet.participantservice.service.imple;

import com.ensa.projet.participantservice.dto.*;
import com.ensa.projet.participantservice.entities.EnrollmentStatus;
import com.ensa.projet.participantservice.entities.Participant;
import com.ensa.projet.participantservice.repository.*;
import com.ensa.projet.participantservice.service.interfaces.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private CertificationRepository certificationRepository;

    @Override
    public ParticipantDTO createParticipant(String userId, KeycloakUserInfo userInfo) {
        Participant participant = Participant.builder()
                .userId(userId)
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .email(userInfo.getEmail())
                .build();

        participant = participantRepository.save(participant);
        return convertToDTO(participant);
    }

    @Override
    public ParticipantDTO updateParticipant(Integer id, ParticipantDTO participantDTO) {
        Optional<Participant> existingParticipant = participantRepository.findById(id);
        if (existingParticipant.isPresent()) {
            Participant participant = existingParticipant.get();
            participant.setFirstName(participantDTO.getFirstName());
            participant.setLastName(participantDTO.getLastName());
            participant.setEmail(participantDTO.getEmail());
            participant.setPhone(participantDTO.getPhone());
            participant.setAddress(participantDTO.getAddress());

            participant = participantRepository.save(participant);
            return convertToDTO(participant);
        }
        return null;
    }

    @Override
    public Optional<ParticipantDTO> getParticipantByUserId(String userId) {
        return participantRepository.findByUserId(userId)
                .map(this::convertToDTO);
    }

    @Override
    public List<CertificationDTO> getParticipantCertifications(Integer participantId) {
        return certificationRepository.findByParticipantId(participantId).stream()
                .map(cert -> CertificationDTO.builder()
                        .id(cert.getId())
                        .participantId(cert.getParticipant().getId())
                        .trainingId(cert.getTrainingId())
                        .certificateNumber(cert.getCertificateNumber())
                        .issueDate(cert.getIssueDate())
                        .expiryDate(cert.getExpiryDate())
                        .finalScore(cert.getFinalScore())
                        .skillsAcquired(cert.getSkillsAcquired())
                        .certificateUrl(cert.getCertificateUrl())
                        .active(cert.isActive())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteParticipant(Integer id) {
        participantRepository.deleteById(id);
    }

    @Override
    public LearningStatsDTO getParticipantLearningStats(Integer participantId) {
        Optional<Participant> participant = participantRepository.findById(participantId);
        if (participant.isPresent()) {
            return LearningStatsDTO.builder()
                    .completedTrainings((int) participant.get().getEnrollments().stream()
                            .filter(e -> e.getStatus() == EnrollmentStatus.COMPLETED).count())
                    .inProgressTrainings((int) participant.get().getEnrollments().stream()
                            .filter(e -> e.getStatus() == EnrollmentStatus.IN_PROGRESS).count())
                    .certifications(participant.get().getCertifications().size())
                    .build();
        }
        return null;
    }



    private ParticipantDTO convertToDTO(Participant participant) {
        return ParticipantDTO.builder()
                .id(participant.getId())
                .userId(participant.getUserId())
                .firstName(participant.getFirstName())
                .lastName(participant.getLastName())
                .email(participant.getEmail())
                .phone(participant.getPhone())
                .address(participant.getAddress())
                .build();
    }
}