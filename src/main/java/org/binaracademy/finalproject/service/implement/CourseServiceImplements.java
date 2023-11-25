package org.binaracademy.finalproject.service.implement;

import org.binaracademy.finalproject.DTO.CourseDTO;
import org.binaracademy.finalproject.model.Course;
import org.binaracademy.finalproject.repository.CourseRepository;
import org.binaracademy.finalproject.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImplements implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Override
    public void addNewCourse(Course course) {
        courseRepository.save(course);
    }

    @Override
    public List<CourseDTO> getAllCourse() {
        return courseRepository.findAll().stream()
                .map(course -> CourseDTO.builder()
                        .about(course.getAboutCourse())
                        .title(course.getTitleCourse())
                        .price(course.getPriceCourse())
                        .categories(course.getCategories())
                        .build())
                .collect(Collectors.toList());
    }
}
