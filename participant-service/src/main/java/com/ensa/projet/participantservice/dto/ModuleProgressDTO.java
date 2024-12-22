package com.ensa.projet.participantservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleProgressDTO {
    private Integer id;
    private Integer moduleId;

    @Min(value = 0, message = "Progress percentage cannot be negative")
    @Max(value = 100, message = "Progress percentage cannot exceed 100")
    private float progressPercentage;

    private LocalDateTime startDate;
    private LocalDateTime completionDate;
    private LocalDateTime lastAccessed;
    private boolean completed;
}
