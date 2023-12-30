package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.Users;
import org.binaracademy.finalproject.model.request.UpdateCourseRequest;
import org.binaracademy.finalproject.model.response.CourseResponse;
import org.binaracademy.finalproject.DTO.CourseDTO;
import org.binaracademy.finalproject.model.response.GetAllCourseAdminResponse;
import org.binaracademy.finalproject.model.response.SubjectResponse;
import org.binaracademy.finalproject.model.Course;
import org.binaracademy.finalproject.model.response.AddCourseResponse;
import org.binaracademy.finalproject.repository.CourseRepository;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.service.CourseService;
import org.binaracademy.finalproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    private CourseResponse toCourseResponse(Course course) {
        return CourseResponse.builder()
                .title(course.getTitleCourse())
                .about(course.getAboutCourse())
                .categories(course.getCategories())
                .code(course.getCodeCourse())
                .level(course.getLevelCourse())
                .price(course.getPriceCourse())
                .isPremium(course.getIsPremium())
                .teacher(course.getTeacher())
                .module(course.getModule())
                .duration(course.getDuration())
                .build();
    }

    private AddCourseResponse toAddCourseResponse(Course course) {
        return AddCourseResponse.builder()
                .about(course.getAboutCourse())
                .title(course.getTitleCourse())
                .categories(course.getCategories())
                .code(course.getCodeCourse())
                .level(course.getLevelCourse())
                .price(course.getPriceCourse())
                .isPremium(course.getIsPremium())
                .teacher(course.getTeacher())
                .module(course.getModule())
                .duration(course.getDuration())
                .linkTelegram(course.getLinkTelegram())
                .build();
    }

    @Override
    public AddCourseResponse addNewCourse(Course course) {
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
        return toAddCourseResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourse() {
        log.info("Getting all of list courses!");
        return courseRepository.findAll().stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponse updateCourse(UpdateCourseRequest updateCourse, String code) {
        try {
            log.info("Process updating course");
            Course courses = courseRepository.findByCodeCourse(code)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            courses.setTitleCourse(updateCourse.getTitle() == null ? courses.getTitleCourse() : updateCourse.getTitle());
            courses.setCodeCourse(updateCourse.getCode() == null ? courses.getCodeCourse() : updateCourse.getCode());
            courses.setAboutCourse(updateCourse.getAbout() == null ? courses.getAboutCourse() : updateCourse.getAbout());
            courses.setLevelCourse(updateCourse.getLevel() == null ? courses.getLevelCourse() : updateCourse.getLevel());
            courses.setPriceCourse(updateCourse.getPrice() == null ? courses.getPriceCourse() : updateCourse.getPrice());
            courses.setIsPremium(updateCourse.getIsPremium() == null ? courses.getIsPremium() : updateCourse.getIsPremium());
            courses.setTeacher(updateCourse.getTeacher() == null ? courses.getTeacher() : updateCourse.getTeacher());
            courses.setCategories(updateCourse.getCategories() == null ? courses.getCategories() : updateCourse.getCategories());
            courses.setDuration(updateCourse.getDuration() == null ? courses.getDuration() : updateCourse.getDuration());
            courses.setModule(updateCourse.getModule() == null ? courses.getModule() : updateCourse.getModule());
            courses.setLinkTelegram(updateCourse.getLinkTelegram() == null ? courses.getLinkTelegram() : updateCourse.getLinkTelegram());
            courseRepository.save(courses);
            log.info("Updating course with code: " + code + " successful!");
            return toCourseResponse(courses);
        } catch (Exception e) {
            log.error("Update course failed");
            throw e;
        }
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
            orderService.deleteByCodeCourse(codeCourse);
            course.getCategories().clear();
            course.getSubjects().clear();
            log.info("Deleting the course with course code: {} successful!", codeCourse);
            courseRepository.deleteByCode(codeCourse);
        } catch (Exception e) {
            log.error("Deleting course failed, please try again!");
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO courseDetailsFromTitle(String titleCourse) {
        log.info("Getting course detail information from course " + titleCourse);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> users = userRepository.findByUsername(username);
        Optional<Course> course = courseRepository.findByTitleCourse(titleCourse);
        Users user = users.get();
        Course course1 = course.get();

        Boolean hasOrder = courseRepository.hasOrder(user.getId(), course1.getId());
        return courseRepository.findByTitleCourse(titleCourse)
                .map(courses -> CourseDTO.builder()
                        .addCourseResponse(AddCourseResponse.builder()
                                .about(courses.getAboutCourse())
                                .code(courses.getCodeCourse())
                                .title(courses.getTitleCourse())
                                .price(courses.getPriceCourse())
                                .teacher(courses.getTeacher())
                                .level(courses.getLevelCourse())
                                .isPremium(courses.getIsPremium())
                                .categories(courses.getCategories())
                                .module(courses.getModule())
                                .duration(courses.getDuration())
                                .linkTelegram(!courses.getIsPremium() ? courses.getLinkTelegram() : hasOrder ? courses.getLinkTelegram() : null)
                                .build())
                        .subjectResponse(courses.getSubjects().stream()
                                .map(subject -> {
                                    SubjectResponse subjectResponse = new SubjectResponse();
                                    subjectResponse.setCode(subject.getCode());
                                    subjectResponse.setTitle(subject.getTitle());
                                    subjectResponse.setDescription(subject.getDescription());
                                    subjectResponse.setIsPremium(subject.getIsPremium());
                                    subjectResponse.setLink(subject.getLinkVideo());
                                    return subjectResponse;
                                })
                                .collect(Collectors.toList()))
                        .build())
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetAllCourseAdminResponse> getAllCourseAdmin() {
        log.info("Getting all of list courses!");
        return courseRepository.findAll().stream()
                .map(course -> GetAllCourseAdminResponse.builder()
                        .id(course.getId())
                        .code(course.getCodeCourse())
                        .title(course.getTitleCourse())
                        .about(course.getAboutCourse())
                        .level(course.getLevelCourse())
                        .price(course.getPriceCourse())
                        .teacher(course.getTeacher())
                        .categorySet(course.getCategories())
                        .isPremium(course.getIsPremium())
                        .module(course.getModule())
                        .duration(course.getDuration())
                        .linkTelegram(course.getLinkTelegram())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourse(String code) {
        log.info("Success getting course where course code: {}", code);
        return courseRepository.findByCodeCourse(code)
                .map(this::toCourseResponse)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getCourseAfterOrder() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> users = userRepository.findByUsername(username);
        Users user = users.get();

        return courseRepository.getCourse(user.getId()).stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CourseResponse> searchingCourse(String title) {
        log.info("Searching course by title!");
        return courseRepository.searchingCourse(title).stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterBackEnd() {
        log.info("Filtering Back End Course!");
        return courseRepository.filterBackEnd().stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterFrontEnd() {
        log.info("Filtering Front End Course!");
        return courseRepository.filterFrontEnd().stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterFullStack() {
        log.info("Filtering Back End Course!");
        return courseRepository.filterFullStack().stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterAdvanced() {
        log.info("Filtering Advanced Level!");
        return courseRepository.filterAdvanced().stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterBeginner() {
        log.info("Filtering Beginner Level!");
        return courseRepository.filterBeginner().stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterIntermediate() {
        log.info("Filtering Intermediate Level!");
        return courseRepository.filterIntermediate().stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterCoursePremium() {
        log.info("Filtering Course Premium!");
        return courseRepository.filterCoursePremium().stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterCourseFree() {
        log.info("Filtering Course Free!");
        return courseRepository.filterCourseFree().stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> searchingCourseAfterOrder(String title) {
        log.info("Searching Course After Order!");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> users = userRepository.findByUsername(username);
        Users user = users.get();

        return courseRepository.searchingCourseAfterOrder(user.getId(), title).stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterAdvancedAfterOrder() {
        log.info("Filtering Advanced level After Order");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> users = userRepository.findByUsername(username);
        Users user = users.get();

        return courseRepository.filterAdvancedAfterOrder(user.getId()).stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterIntermediateAfterOrder() {
        log.info("Filtering Intermediate level After Order");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> users = userRepository.findByUsername(username);
        Users user = users.get();

        return courseRepository.filterIntermediateAfterOrder(user.getId()).stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterBeginnerAfterOrder() {
        log.info("Filtering Beginner level After Order");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> users = userRepository.findByUsername(username);
        Users user = users.get();

        return courseRepository.filterBeginnerAfterOrder(user.getId()).stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }
}