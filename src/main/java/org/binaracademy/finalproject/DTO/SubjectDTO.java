package org.binaracademy.finalproject.DTO;

import lombok.Builder;
import lombok.Data;
import org.binaracademy.finalproject.model.Course;

@Data
@Builder
public class SubjectDTO {

    private String title;
    private String link;
    private String description;
    private Boolean isPremium;
}
