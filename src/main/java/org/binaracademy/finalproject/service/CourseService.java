package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.request.UpdateCourseRequest;
import org.binaracademy.finalproject.model.response.CourseResponse;
import org.binaracademy.finalproject.DTO.CourseDTO;
import org.binaracademy.finalproject.model.Course;
import org.binaracademy.finalproject.model.response.AddCourseResponse;
import org.binaracademy.finalproject.model.response.GetAllCourseAdminResponse;

import java.util.List;

public interface CourseService {

    AddCourseResponse addNewCourse (Course course);

    List<CourseResponse> getAllCourse();

    CourseResponse updateCourse(UpdateCourseRequest updateCourse, String code);

    void deleteCourseByCode(String codeCourse);

    CourseDTO courseDetails(String titleCourse);

    List<GetAllCourseAdminResponse> getAllCourseAdmin();
}