package com.ensa.projet.participantservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "module_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private TrainingEnrollment enrollment;

    private Integer moduleId;

    private float progressPercentage;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;

    private boolean completed;
}
