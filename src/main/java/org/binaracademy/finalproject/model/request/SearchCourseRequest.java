package org.binaracademy.finalproject.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCourseRequest {

    private String name;
    private String about;

    @NotNull
    private int page;

    @NotNull
    private int size;
}