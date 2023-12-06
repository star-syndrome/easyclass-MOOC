package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.DTO.ResponseDTO;
import org.binaracademy.finalproject.model.response.SubjectResponse;
import org.binaracademy.finalproject.model.Subject;
import org.binaracademy.finalproject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> addSubject(@RequestBody Subject subject){
        try {
            return ResponseDTO.statusResponse(HttpStatus.OK,
                    "Add new subject successful",
                    subjectService.addSubject(subject));
        } catch (Exception e) {
            return ResponseDTO.internalServerError(e.getMessage());
        }
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllSubject(){
        try {
            return ResponseDTO.statusResponse(HttpStatus.OK,
                    "Success get all subject",
                    subjectService.getAllSubject());
        } catch (Exception e) {
            return ResponseDTO.internalServerError(e.getMessage());
        }
    }

    @PutMapping(value = "/update/{code}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateSubject(@PathVariable String code, @RequestBody Subject subject){
        try {
            return ResponseDTO.statusResponse(HttpStatus.OK,
                    "Update subject successful!",
                    subjectService.updateSubject(subject, code));
        } catch (Exception e) {
            return ResponseDTO.internalServerError(e.getMessage());
        }
    }

    @DeleteMapping(value = "delete/{code}")
    public ResponseEntity<Object> deleteSubject(@PathVariable String code){
        try {
            subjectService.deleteSubjectByCode(code);
            return ResponseDTO.statusResponse(HttpStatus.OK,
                    "Deleting subject successful!",
                    null);
        } catch (Exception e) {
            return ResponseDTO.internalServerError(e.getMessage());
        }
    }
}