package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.model.response.CourseResponse;
import org.binaracademy.finalproject.model.response.ResponseController;
import org.binaracademy.finalproject.DTO.CourseDTO;
import org.binaracademy.finalproject.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCourse(@RequestParam String code) {
        CourseResponse courseResponse = courseService.getCourse(code);
        try {
            if (Objects.nonNull(courseResponse)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Success get course " + code, courseService.getCourse(code));
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
}