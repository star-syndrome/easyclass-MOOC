package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.model.response.CourseResponse;
import org.binaracademy.finalproject.DTO.CourseDTO;
import org.binaracademy.finalproject.model.Course;
import org.binaracademy.finalproject.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addCourse(@RequestBody Course course){
        courseService.addNewCourse(course);
        return ResponseEntity.ok()
                .body("Add new course successfully!");
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseResponse>> getAllCourse(){
        return ResponseEntity.ok()
                .body(courseService.getAllCourse());
    }

    @DeleteMapping(value = "/delete/{codeCourse}")
    public ResponseEntity<String> deleteCourse(@PathVariable String codeCourse){
        courseService.deleteCourseByCode(codeCourse);
        return ResponseEntity.ok().body("Course with code: " + codeCourse + " successfully deleted");
    }

    @PutMapping(value = "/update/{code}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable String code, @RequestBody Course course){
        return ResponseEntity.ok()
                .body(courseService.updateCourse(course,code));
    }

    @GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseDTO> detailsCourse(@RequestParam String title) {
        return ResponseEntity.ok()
                .body(courseService.courseDetails(title));
    }
}