package com.ensa.projet.participantservice.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningStatsDTO {
    private Integer completedTrainings;
    private Integer inProgressTrainings;
    private Integer certifications;
    private Long totalLearningTimeMinutes;
    private Float averageCompletionRate;
    private Integer totalSkillsAcquired;
}
