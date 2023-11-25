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
    private Double price;
    private Set<Category> categories;

}