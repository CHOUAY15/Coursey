package com.ensa.projet.participantservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificationDTO {
    private Integer id;

    @NotNull(message = "Participant ID is required")
    private Integer participantId;

    @NotNull(message = "Training ID is required")
    private Integer trainingId;

    @NotBlank(message = "Certificate number is required")
    private String certificateNumber;

    private LocalDateTime issueDate;
    private LocalDateTime expiryDate;
    private float finalScore;

    @Builder.Default
    private List<String> skillsAcquired = new ArrayList<>();

    private String certificateUrl;
    private boolean active;
}