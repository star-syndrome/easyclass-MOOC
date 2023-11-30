package org.binaracademy.finalproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binaracademy.finalproject.model.response.CourseResponse;
import org.binaracademy.finalproject.model.response.SubjectResponse;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private CourseResponse courseResponse;
    private List<SubjectResponse> subjectResponse;
    private String aboutCourse;
}