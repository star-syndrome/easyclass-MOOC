package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.DTO.ResponseDTO;
import org.binaracademy.finalproject.model.request.UpdateUserRequest;
import org.binaracademy.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllCourse(){
        try {
            return ResponseDTO.statusResponse(HttpStatus.OK,
                    "Success get all user",
                    userService.getAllUser());
        } catch (Exception e) {
            return ResponseDTO.internalServerError(e.getMessage());
        }
    }

    @PutMapping(value = "/update/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUser(@PathVariable String username, @Valid @RequestBody UpdateUserRequest users) {
        try {
            return ResponseDTO.statusResponse(HttpStatus.OK,
                    "Update user successful!",
                    userService.updateUsers(users, username));
        } catch (Exception e) {
            return ResponseDTO.internalServerError(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{username}")
    public ResponseEntity<Object> deleteCourse(@PathVariable String username){
        try {
            userService.deleteUsersByUsername(username);
            return ResponseDTO.statusResponse(HttpStatus.OK,
                    "Deleting user successful!",
                    null);
        } catch (Exception e) {
            return ResponseDTO.internalServerError(e.getMessage());
        }
    }
}