package org.binaracademy.finalproject.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binaracademy.finalproject.model.Category;
import org.binaracademy.finalproject.model.Course;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCourseRequest {

    @NotBlank
    private String code;

    private String about;
    private String title;
    private Double price;
    private String level;
    private String teacher;
    private Boolean isPremium;
    private Set<Category> categories;
    private String module;
    private String duration;
    private String link;
}