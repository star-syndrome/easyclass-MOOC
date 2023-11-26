package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.DTO.CourseDTO;
import org.binaracademy.finalproject.model.Course;
import org.binaracademy.finalproject.repository.CourseRepository;
import org.binaracademy.finalproject.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CourseServiceImplements implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Override
    public void addNewCourse(Course course) {
        log.info("Process of adding new courses!");
        Optional.ofNullable(course)
                .map(newProduct -> courseRepository.save(course))
                .map(result -> {
                    boolean isSuccess = true;
                    log.info("Successfully added a new courses with name: {}", course.getTitleCourse());
                    return isSuccess;
                })
                .orElseGet(() -> {
                    log.info("Failed to add new courses");
                    return Boolean.FALSE;
                });
        assert course != null;
        log.info("Process of adding a new courses is complete, new courses: {}", course.getTitleCourse());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCourse() {
        log.info("Getting all of list courses!");
        return courseRepository.findAll().stream()
                .map(course -> CourseDTO.builder()
                        .title(course.getTitleCourse())
                        .categories(course.getCategories())
                        .code(course.getCodeCourse())
                        .level(course.getLevelCourse())
                        .price(course.getPriceCourse())
                        .isPremium(course.getIsPremium())
                        .teacher(course.getTeacher())
                        .about(course.getAboutCourse())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCourseByCourseCode(String codeCourse) {
        try {
            log.info("Process of deleting a courses");
            Course course = courseRepository.findByCodeCourse(codeCourse);
            if (!Optional.ofNullable(course).isPresent()){
                log.info("Courses is not available");
            }
            courseRepository.deleteByCourseCode(codeCourse);
            log.info("Deleting courses with course code: {} successful!", codeCourse);
        } catch (Exception e) {
            log.error("Deleting courses failed, please try again!");
            throw e;
        }
    }
}