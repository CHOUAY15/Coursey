package com.ensa.projet.participantservice.dto;


import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentProgressDTO {
    private Integer id;
    private Integer contentId;
    private String contentType;
    private LocalDateTime completionDate;
    private boolean completed;
    private Long timeSpentSeconds;
    private String notes;
}