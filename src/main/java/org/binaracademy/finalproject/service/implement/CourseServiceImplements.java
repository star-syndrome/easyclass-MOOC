package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.Users;
import org.binaracademy.finalproject.model.request.CreateCourseRequest;
import org.binaracademy.finalproject.model.request.UpdateCourseRequest;
import org.binaracademy.finalproject.model.response.*;
import org.binaracademy.finalproject.DTO.CourseDTO;
import org.binaracademy.finalproject.model.Course;
import org.binaracademy.finalproject.repository.CourseRepository;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.service.CourseService;
import org.binaracademy.finalproject.service.OrderService;
import org.binaracademy.finalproject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private SubjectService subjectService;

    private CourseResponse toCourseResponse(Course course) {
        return CourseResponse.builder()
                .title(course.getTitle())
                .about(course.getAbout())
                .categories(course.getCategories())
                .code(course.getCode())
                .level(course.getLevel())
                .price(course.getPrice())
                .isPremium(course.getIsPremium())
                .teacher(course.getTeacher())
                .module(course.getModule())
                .duration(course.getDuration())
                .build();
    }

    private AddCourseResponse toAddCourseResponse(Course course) {
        return AddCourseResponse.builder()
                .about(course.getAbout())
                .title(course.getTitle())
                .categories(course.getCategories())
                .code(course.getCode())
                .level(course.getLevel())
                .price(course.getPrice())
                .isPremium(course.getIsPremium())
                .teacher(course.getTeacher())
                .module(course.getModule())
                .duration(course.getDuration())
                .link(course.getLinkTelegram())
                .build();
    }

    @Override
    public AddCourseResponse addNewCourse(CreateCourseRequest request) {
        try {
            log.info("Process of adding new course");
            if (courseRepository.existsByCode(request.getCode())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course already exists");
            }

            Course course = new Course();
            course.setCode(request.getCode());
            course.setTitle(request.getTitle());
            course.setAbout(request.getAbout());
            course.setPrice(request.getPrice());
            course.setTeacher(request.getTeacher());
            course.setLevel(request.getLevel());
            course.setCategories(request.getCategories());
            course.setModule(request.getModule());
            course.setDuration(request.getDuration());
            course.setLinkTelegram(request.getLink());
            course.setIsPremium(request.getIsPremium());

            courseRepository.save(course);
            log.info("Process of adding a new course is completed, new course: {}", course.getTitle());

            return toAddCourseResponse(course);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw e;
        }
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
            Course courses = courseRepository.findByCode(code)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found!"));

            courses.setTitle(updateCourse.getTitle() == null ? courses.getTitle() : updateCourse.getTitle());
            courses.setCode(updateCourse.getCode() == null ? courses.getCode() : updateCourse.getCode());
            courses.setAbout(updateCourse.getAbout() == null ? courses.getAbout() : updateCourse.getAbout());
            courses.setLevel(updateCourse.getLevel() == null ? courses.getLevel() : updateCourse.getLevel());
            courses.setPrice(updateCourse.getPrice() == null ? courses.getPrice() : updateCourse.getPrice());
            courses.setIsPremium(updateCourse.getIsPremium() == null ? courses.getIsPremium() : updateCourse.getIsPremium());
            courses.setTeacher(updateCourse.getTeacher() == null ? courses.getTeacher() : updateCourse.getTeacher());
            courses.setCategories(updateCourse.getCategories() == null ? courses.getCategories() : updateCourse.getCategories());
            courses.setModule(updateCourse.getModule() == null ? courses.getModule() : updateCourse.getModule());
            courses.setDuration(updateCourse.getDuration() == null ? courses.getDuration() : updateCourse.getDuration());
            courses.setLinkTelegram(updateCourse.getLink() == null ? courses.getLinkTelegram() : updateCourse.getLink());

            courseRepository.save(courses);
            log.info("Updating course with code: " + code + " successful!");

            return toCourseResponse(courses);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteCourseByCode(String codeCourse) {
        try {
            log.info("Process of deleting a course");
            Course course = courseRepository.findByCode(codeCourse)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found!"));

            course.getCategories().clear();
            subjectService.deleteByCourseCode(course.getCode());
            orderService.deleteByCode(course.getCode());

            courseRepository.deleteByCode(course.getCode());
            log.info("Deleting the course with course code: {} successful!", course.getCode());

        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO courseDetailsFromCode(String code) {
        try {
            log.info("Getting course detail information from course code: " + code);
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Users> users = userRepository.findByEmail(email);
            Optional<Course> course = courseRepository.findByCode(code);

            Boolean hasOrder = courseRepository.hasOrder(users.get().getId(), course.get().getId());
            return courseRepository.findByCode(code)
                    .map(courses -> CourseDTO.builder()
                            .addCourseResponse(AddCourseResponse.builder()
                                    .about(courses.getAbout())
                                    .code(courses.getCode())
                                    .title(courses.getTitle())
                                    .price(courses.getPrice())
                                    .teacher(courses.getTeacher())
                                    .level(courses.getLevel())
                                    .isPremium(courses.getIsPremium())
                                    .categories(courses.getCategories())
                                    .module(courses.getModule())
                                    .duration(courses.getDuration())
                                    .link(!courses.getIsPremium() ? courses.getLinkTelegram() : hasOrder ? courses.getLinkTelegram() : null)
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
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found!"));
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetAllCourseAdminResponse> getAllCourseAdmin() {
        log.info("Getting all of list courses!");
        return courseRepository.findAll().stream()
                .map(course -> GetAllCourseAdminResponse.builder()
                        .id(course.getId())
                        .code(course.getCode())
                        .title(course.getTitle())
                        .about(course.getAbout())
                        .level(course.getLevel())
                        .price(course.getPrice())
                        .teacher(course.getTeacher())
                        .categorySet(course.getCategories())
                        .isPremium(course.getIsPremium())
                        .module(course.getModule())
                        .duration(course.getDuration())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponseTele getCourse(String code) {
        log.info("Success getting course where course code: {}", code);
        return courseRepository.findByCode(code)
                .map(course2 -> CourseResponseTele.builder()
                        .about(course2.getAbout())
                        .title(course2.getTitle())
                        .code(course2.getCode())
                        .level(course2.getLevel())
                        .price(course2.getPrice())
                        .teacher(course2.getTeacher())
                        .categories(course2.getCategories())
                        .isPremium(course2.getIsPremium())
                        .duration(course2.getDuration())
                        .module(course2.getModule())
                        .link(course2.getLinkTelegram())
                        .build())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found!"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getCourseAfterOrder() {
        try {
            log.info("Trying to get data course after order!");
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Users users = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

            return courseRepository.getCourse(users.getId()).stream()
                    .map(this::toCourseResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error : " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
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
    public Page<CourseResponse> getAllCoursePagination(int page) {
        log.info("Trying to get all course with pagination");
        try {
            Page<Course> courses = courseRepository.getAllCourseWithPagination(PageRequest.of((page - 1), 3));
            return courses.map(this::toCourseResponse);
        } catch (Exception e) {
            log.error("Error getting course with pagination");
            throw e;
        }
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
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> users = userRepository.findByEmail(email);

        return courseRepository.searchingCourseAfterOrder(users.get().getId(), title).stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filteringBackendAfterOrder() {
        log.info("Filtering Backend After Order");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> users = userRepository.findByEmail(email);

        return courseRepository.filterBackendAfterOrder(users.get().getId()).stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filteringFrontendAfterOrder() {
        log.info("Filtering Frontend After Order");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> users = userRepository.findByEmail(email);

        return courseRepository.filterFrontendAfterOrder(users.get().getId()).stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filteringFullstackAfterOrder() {
        log.info("Filtering Fullstack After Order");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> users = userRepository.findByEmail(email);

        return courseRepository.filterFullstackAfterOrder(users.get().getId()).stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }
}