package com.ensa.projet.participantservice.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "content_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private TrainingEnrollment enrollment;

    private Integer contentId;

    private String contentType;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    private boolean completed;

    @Column(name = "time_spent_seconds")
    private Long timeSpentSeconds;

    @Column(length = 1000)
    private String notes;
}
