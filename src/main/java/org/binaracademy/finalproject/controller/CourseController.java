package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.DTO.ResponseDTO;
import org.binaracademy.finalproject.DTO.CourseDTO;
import org.binaracademy.finalproject.model.Course;
import org.binaracademy.finalproject.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addCourse(@Valid @RequestBody Course course){
       try {
           return ResponseDTO.statusResponse(HttpStatus.OK,
                   "Adding new course successful!",
                   courseService.addNewCourse(course));
       } catch (Exception e) {
           return ResponseDTO.internalServerError(e.getMessage());
       }
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllCourse(){
        try {
            return ResponseDTO.statusResponse(HttpStatus.OK,
                    "Success get all course",
                    courseService.getAllCourse());
        } catch (Exception e) {
            return ResponseDTO.internalServerError(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{codeCourse}")
    public ResponseEntity<Object> deleteCourse(@PathVariable String codeCourse){
        try {
            courseService.deleteCourseByCode(codeCourse);
            return ResponseDTO.statusResponse(HttpStatus.OK,
                    "Deleting course successful!",
                    null);
        } catch (Exception e) {
            return ResponseDTO.internalServerError(e.getMessage());
        }
    }

    @PutMapping(value = "/update/{code}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateCourse(@PathVariable String code, @RequestBody Course course){
        try {
            return ResponseDTO.statusResponse(HttpStatus.OK,
                    "Update course successful!",
                    courseService.updateCourse(course, code));
        } catch (Exception e) {
            return ResponseDTO.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> detailsCourse(@RequestParam String title) {
        CourseDTO courseDTO = courseService.courseDetails(title);
        try {
            if (Objects.nonNull(courseDTO)) {
                return ResponseDTO.statusResponse(HttpStatus.OK,
                        "Success getting all information about course " + title,
                        courseService.courseDetails(title));
            }
            return ResponseDTO.statusResponse(HttpStatus.NOT_FOUND,
                    "Course not found!",
                    null);
        } catch (Exception e) {
            return ResponseDTO.internalServerError(e.getMessage());
        }
    }
}