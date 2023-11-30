package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.response.CourseResponse;
import org.binaracademy.finalproject.DTO.CourseDTO;
import org.binaracademy.finalproject.model.Course;

import java.util.List;

public interface CourseService {

    void addNewCourse (Course course);

    List<CourseResponse> getAllCourse();

    CourseResponse updateCourse(Course course, String code);

    void deleteCourseByCode(String codeCourse);

    CourseDTO courseDetails(String titleCourse);
}