package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.DTO.CourseDTO;
import org.binaracademy.finalproject.model.Course;

import java.util.List;

public interface CourseService {

    void addNewCourse (Course course);

    List<CourseDTO> getAllCourse();

    // Bikin Update Courses

    void deleteCourseByCourseCode(String codeCourse);
}
