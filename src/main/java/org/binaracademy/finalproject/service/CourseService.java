package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.request.UpdateCourseRequest;
import org.binaracademy.finalproject.model.response.CourseResponse;
import org.binaracademy.finalproject.DTO.CourseDTO;
import org.binaracademy.finalproject.model.Course;
import org.binaracademy.finalproject.model.response.AddCourseResponse;
import org.binaracademy.finalproject.model.response.GetAllCourseAdminResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {

    AddCourseResponse addNewCourse (Course course);

    List<CourseResponse> getAllCourse();

    CourseResponse updateCourse(UpdateCourseRequest updateCourse, String code);

    void deleteCourseByCode(String codeCourse);

    CourseDTO courseDetailsFromTitle(String titleCourse);

    List<GetAllCourseAdminResponse> getAllCourseAdmin();

    CourseResponse getCourse(String code);

    List<CourseResponse> getCourseAfterOrder();

    List<CourseResponse> searchingCourse(String title);

    List<CourseResponse> filterBackEnd();

    List<CourseResponse> filterFrontEnd();

    List<CourseResponse> filterFullStack();

    Page<CourseResponse> getAllCoursePagination(int page);

    List<CourseResponse> filterAdvanced();

    List<CourseResponse> filterBeginner();

    List<CourseResponse> filterIntermediate();

    List<CourseResponse> filterCoursePremium();

    List<CourseResponse> filterCourseFree();

    List<CourseResponse> searchingCourseAfterOrder(String title);

    List<CourseResponse> filterAdvancedAfterOrder();

    List<CourseResponse> filterIntermediateAfterOrder();

    List<CourseResponse> filterBeginnerAfterOrder();
}