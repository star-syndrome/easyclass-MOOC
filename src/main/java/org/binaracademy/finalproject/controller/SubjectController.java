package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.model.response.SubjectResponse;
import org.binaracademy.finalproject.model.Subject;
import org.binaracademy.finalproject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addSubject(@RequestBody Subject subject){
        subjectService.addSubject(subject);
        return ResponseEntity.ok()
                .body("Add new subject successful!");
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SubjectResponse>> getAllSubject(){
        return ResponseEntity.ok()
                .body(subjectService.getAllSubject());
    }

    @PutMapping(value = "/update/{title}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubjectResponse> updateSubject(@PathVariable String title, @RequestBody Subject subject){
        return ResponseEntity.ok()
                .body(subjectService.updateSubject(subject, title));
    }

    @DeleteMapping(value = "delete/{title}")
    public ResponseEntity<String> deleteSubject(@PathVariable String code){
        subjectService.deleteSubjectByCode(code);
        return ResponseEntity.ok()
                .body("Subject with code: " + code + " successfully deleted");
    }
}