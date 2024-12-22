package com.ensa.projet.participantservice.dto;

import com.ensa.projet.participantservice.entities.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
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
public class EnrollmentDTO {
    private Integer id;

    @NotNull(message = "Participant ID is required")
    private Integer participantId;

    @NotNull(message = "Training ID is required")
    private Integer trainingId;

    private LocalDateTime enrollmentDate;
    private LocalDateTime lastAccessed;
    private LocalDateTime completionDate;
    private EnrollmentStatus status;

    @Min(value = 0, message = "Progress cannot be negative")
    private float overallProgress;

    @Builder.Default
    private List<ModuleProgressDTO> moduleProgresses = new ArrayList<>();
}