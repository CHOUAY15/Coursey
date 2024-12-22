package com.ensa.projet.trainingservice.model.dao;

import com.ensa.projet.trainingservice.model.entities.ContentType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentDTO {
    private Integer id;
    private String title;
    private String description;
    private ContentType type;
    private String url;
    private Integer orderIndex;
}
