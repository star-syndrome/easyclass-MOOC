package org.binaracademy.finalproject.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSubjectRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String code;

    @NotBlank
    private String description;

    @NotBlank
    private String linkVideo;

    @NotNull
    private Boolean isPremium;
}