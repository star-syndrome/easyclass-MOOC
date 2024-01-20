package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.model.response.CourseResponse;
import org.binaracademy.finalproject.model.response.ResponseController;
import org.binaracademy.finalproject.DTO.CourseDTO;
import org.binaracademy.finalproject.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllCourse() {
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success get all course", courseService.getAllCourse());
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/{page}")
    public ResponseEntity<Object> getAllCourseWithPagination(@PathVariable int page){
        Page<CourseResponse> courseResponse = courseService.getAllCoursePagination(page);
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Get all course with pagination success!", courseService.getAllCoursePagination(page));
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/detailsFromTitle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> detailsCourse(@RequestParam String title) {
        CourseDTO courseDTO = courseService.courseDetailsFromTitle(title);
        try {
            if (Objects.nonNull(courseDTO)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Success getting all information about course " + title,
                        courseService.courseDetailsFromTitle(title));
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/getCourseOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCourseOrder() {
        List<CourseResponse> courseResponse = courseService.getCourseAfterOrder();
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Success get course!", courseService.getCourseAfterOrder());
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/searchingCourseByTitle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> searchingCourse(@RequestParam String title) {
        List<CourseResponse> courseResponse = courseService.searchingCourse(title);
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Searching course by title success!", courseService.searchingCourse(title));
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/filterBackEnd", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> filterBackend() {
        List<CourseResponse> courseResponse = courseService.filterBackEnd();
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Filtering Back End Success!", courseService.filterBackEnd());
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/filterFrontEnd", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> filterFrontend() {
        List<CourseResponse> courseResponse = courseService.filterFrontEnd();
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Filtering Front End Success!", courseService.filterFrontEnd());
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/filterFullStack", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> filterFullStack() {
        List<CourseResponse> courseResponse = courseService.filterFullStack();
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Filtering Full Stack Success!", courseService.filterFullStack());
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/filterAdvanced", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> filterAdvancedLevel() {
        List<CourseResponse> courseResponse = courseService.filterAdvanced();
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Filter Advanced Level Success!", courseService.filterAdvanced());
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/filterIntermediate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> filterIntermediateLevel() {
        List<CourseResponse> courseResponse = courseService.filterIntermediate();
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Filter Intermediate Level Success!", courseService.filterIntermediate());
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/filterBeginner", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> filterBeginnerLevel() {
        List<CourseResponse> courseResponse = courseService.filterBeginner();
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Filter Beginner Level Success!", courseService.filterBeginner());
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/filterCoursePremium", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> filterCoursePremium() {
        List<CourseResponse> courseResponse = courseService.filterCoursePremium();
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Filter Course Premium Success!", courseService.filterCoursePremium());
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/filterCourseFree", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> filterCourseFree() {
        List<CourseResponse> courseResponse = courseService.filterCourseFree();
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Filter Course Free Success!", courseService.filterCourseFree());
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/searchingCourseAfterOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> searchingCourseAfterOrder(@RequestParam String title) {
        List<CourseResponse> courseResponse = courseService.searchingCourseAfterOrder(title);
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Searching course after order by title success!", courseService.searchingCourseAfterOrder(title));
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/filterBackendAfterOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> filteringBackendAfterOrder() {
        List<CourseResponse> courseResponse = courseService.filterBackendAfterOrder();
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Filtering Backend After Order Success!", courseService.filterBackendAfterOrder());
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/filterFrontendAfterOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> filteringFrontendAfterOrder() {
        List<CourseResponse> courseResponse = courseService.filterFrontendAfterOrder();
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Filtering Frontend After Order Success!", courseService.filterFrontendAfterOrder());
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/filterFullstackAfterOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> filteringFullstackAfterOrder() {
        List<CourseResponse> courseResponse = courseService.filterFullstackAfterOrder();
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Filtering Fullstack After Order Success!", courseService.filterFullstackAfterOrder());
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }
}