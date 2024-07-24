package org.binaracademy.finalproject.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binaracademy.finalproject.model.Category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String code;

    @NotBlank
    private String about;

    @NotBlank
    private String level;

    @NotBlank
    private String teacher;

    @NotNull
    private Double price;

    @NotNull
    private Boolean isPremium;

    @NotNull
    private Set<Category> categories;

    @NotBlank
    private String module;

    @NotBlank
    private String duration;

    @NotBlank
    private String link;
}