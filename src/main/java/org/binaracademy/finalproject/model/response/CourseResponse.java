package org.binaracademy.finalproject.model.response;

import lombok.Builder;
import lombok.Data;
import org.binaracademy.finalproject.model.Category;

import java.util.Set;

@Data
@Builder
public class CourseResponse {

    private String title;
    private String code;
    private Double price;
    private String level;
    private String teacher;
    private Boolean isPremium;
    private Set<Category> categories;
}