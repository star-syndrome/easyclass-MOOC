package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.DTO.SubjectDTO;
import org.binaracademy.finalproject.model.Subject;
import org.binaracademy.finalproject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @PostMapping(value = "/add", consumes = "application/json")
    public ResponseEntity<String> addSubject(@RequestBody Subject subject){
        subjectService.addSubject(subject);
        return ResponseEntity.ok()
                .body("Add new subject successful!");
    }

    @GetMapping(value = "/get", produces = "application/json")
    public ResponseEntity<List<SubjectDTO>> getAllSubject(){
        return ResponseEntity.ok()
                .body(subjectService.getAllSubject());
    }

    @PutMapping(value = "/update/{title}")
    public ResponseEntity<SubjectDTO> updateSubject(@PathVariable String title,
                                                    @RequestBody Subject subject){
        return ResponseEntity.ok()
                .body(subjectService.updateSubject(subject, title));
    }

    @DeleteMapping(value = "delete/{title}")
    public ResponseEntity<String> deleteSubject(@PathVariable String title){
        subjectService.deleteSubjectByTitle(title);
        return ResponseEntity.ok()
                .body("Subject with title: " + title + " successfully deleted");
    }
}