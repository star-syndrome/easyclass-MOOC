package org.binaracademy.finalproject.model.response;

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
public class CourseResponseTele {

    private String title;
    private String about;
    private String code;
    private Double price;
    private String level;
    private String teacher;
    private Boolean isPremium;
    private Set<Category> categories;
    private String module;
    private String duration;
    private String linkTelegram;
}