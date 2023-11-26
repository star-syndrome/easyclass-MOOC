package org.binaracademy.finalproject.DTO;

import lombok.Builder;
import lombok.Data;
import org.binaracademy.finalproject.model.Category;

import java.util.Set;

@Data
@Builder
public class CourseDTO {

    private String about;
    private String title;
    private String code;
    private Double price;
    private String level;
    private String teacher;
    private Boolean isPremium;
    private Set<Category> categories;
}