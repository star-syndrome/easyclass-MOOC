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

    private CourseDTO toCourseDTO(Course course) {
        return CourseDTO.builder()
                .title(course.getTitleCourse())
                .categories(course.getCategories())
                .code(course.getCodeCourse())
                .level(course.getLevelCourse())
                .price(course.getPriceCourse())
                .isPremium(course.getIsPremium())
                .teacher(course.getTeacher())
                .build();
    }

    @Override
    public void addNewCourse(Course course) {
        log.info("Process of adding new course");
        Optional.ofNullable(course)
                .map(newProduct -> courseRepository.save(course))
                .map(result -> {
                    boolean isSuccess = true;
                    log.info("Successfully added a new course with name: {}", course.getTitleCourse());
                    return isSuccess;
                })
                .orElseGet(() -> {
                    log.info("Failed to add new course");
                    return Boolean.FALSE;
                });
        assert course != null;
        log.info("Process of adding a new course is completed, new course: {}", course.getTitleCourse());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCourse() {
        log.info("Getting all of list courses!");
        return courseRepository.findAll().stream()
                .map(this::toCourseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDTO updateCourse(Course course, String code) {
        log.info("Process updating course");
        Course course1 = courseRepository.findByCodeCourse(code)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course1.setTitleCourse(course.getTitleCourse());
        course1.setCodeCourse(course.getCodeCourse());
        course1.setAboutCourse(course.getAboutCourse());
        course1.setLevelCourse(course.getLevelCourse());
        course1.setPriceCourse(course.getPriceCourse());
        course1.setIsPremium(course.getIsPremium());
        course1.setTeacher(course.getTeacher());
        course1.setCategories(course.getCategories());
        courseRepository.save(course1);
        log.info("Updating course with code: " + code + " successful!");

        return toCourseDTO(course1);
    }

    @Override
    public void deleteCourseByCode(String codeCourse) {
        try {
            log.info("Process of deleting a course");
            Course course = courseRepository.findByCodeCourse(codeCourse).orElse(null);
            if (!Optional.ofNullable(course).isPresent()){
                log.info("Course is not available");
            }
            assert course != null;
            course.getCategories().clear();
            course.getSubjects().clear();
            courseRepository.deleteByCode(codeCourse);
            log.info("Deleting the course with course code: {} successful!", codeCourse);
        } catch (Exception e) {
            log.error("Deleting course failed, please try again!");
            throw e;
        }
    }
}