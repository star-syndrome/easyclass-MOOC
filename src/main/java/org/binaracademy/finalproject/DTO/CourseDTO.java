package org.binaracademy.finalproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binaracademy.finalproject.model.response.SubjectResponse;
import org.binaracademy.finalproject.model.response.AddCourseResponse;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private AddCourseResponse addCourseResponse;
    private List<SubjectResponse> subjectResponse;
}