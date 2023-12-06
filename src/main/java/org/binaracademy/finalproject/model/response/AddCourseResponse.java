package org.binaracademy.finalproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binaracademy.finalproject.model.Category;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseResponse {

    private String about;
    private String title;
    private String code;
    private Double price;
    private String level;
    private String teacher;
    private Boolean isPremium;
    private Set<Category> categories;
}