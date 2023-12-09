package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.model.response.ResponseController;
import org.binaracademy.finalproject.DTO.CourseDTO;
import org.binaracademy.finalproject.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllCourse(){
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success get all course",
                    courseService.getAllCourse());
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> detailsCourse(@RequestParam String title) {
        CourseDTO courseDTO = courseService.courseDetails(title);
        try {
            if (Objects.nonNull(courseDTO)) {
                return ResponseController.statusResponse(HttpStatus.OK,
                        "Success getting all information about course " + title,
                        courseService.courseDetails(title));
            }
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!", null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }
}