package org.binaracademy.finalproject.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binaracademy.finalproject.model.Course;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSubjectRequest {

    @NotBlank
    private Course course;

    @NotBlank
    private String code;

    @NotBlank
    private String title;

    @NotBlank
    private String linkVideo;

    @NotBlank
    private String description;

    @NotNull
    private Boolean isPremium;
}