package com.ensa.projet.participantservice.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EnrollmentSummaryDTO {
    private Integer participantId;
    private Integer trainingId;
    private String trainingTitle;
    private String categoryName;
    private float progress;
    private String status;
    private boolean certified;
}
