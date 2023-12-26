package org.binaracademy.finalproject.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binaracademy.finalproject.model.Category;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseRequest {

    private String title;
    private String code;
    private String about;
    private String level;
    private String teacher;
    private Double price;
    private Boolean isPremium;
    private Set<Category> categories;
    private String module;
    private String duration;
    private String link;
}