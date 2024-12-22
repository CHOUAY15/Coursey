package com.ensa.projet.participantservice.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDTO {
    private Integer id;
    private String title;
    private String description;
    private List<String> instructions;

    private List<QuizDTO> quizzes;
}