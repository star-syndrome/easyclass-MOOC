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
public class GetAllCourseAdminResponse {

    private String id;
    private String code;
    private Double price;
    private String title;
    private String teacher;
    private String level;
    private Boolean isPremium;
    private String about;
    private Set<Category> categorySet;
    private String module;
    private String duration;
    private String linkTelegram;
}