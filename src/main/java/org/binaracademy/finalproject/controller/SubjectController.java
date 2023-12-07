package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.model.request.UpdateSubjectRequest;
import org.binaracademy.finalproject.model.response.ResponseController;
import org.binaracademy.finalproject.model.Subject;
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
    SubjectService subjectService;

    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addSubject(@RequestBody Subject subject){
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Add new subject successful",
                    subjectService.addSubject(subject));
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

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

    @PutMapping(value = "/update/{code}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateSubject(@PathVariable String code, @RequestBody UpdateSubjectRequest subject){
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Update subject successful!", subjectService.updateSubject(subject, code));
        } catch (RuntimeException runtimeException) {
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    runtimeException.getMessage(), null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{code}")
    public ResponseEntity<Object> deleteSubject(@PathVariable String code){
        try {
            subjectService.deleteSubjectByCode(code);
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Deleting subject successful!", null);
        } catch (RuntimeException runtimeException) {
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    runtimeException.getMessage(), null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }
}