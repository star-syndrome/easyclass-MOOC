package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.model.response.ResponseController;
import org.binaracademy.finalproject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllSubject(){
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success get all subject",
                    subjectService.getAllSubject());
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }
}