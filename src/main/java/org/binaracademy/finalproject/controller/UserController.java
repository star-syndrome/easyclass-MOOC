package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.model.response.UserResponse;
import org.binaracademy.finalproject.model.request.UpdateUserRequest;
import org.binaracademy.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PutMapping(value = "/update/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUser(@PathVariable String username, @Valid @RequestBody UpdateUserRequest users) {
        return ResponseEntity.ok()
                .body(userService.updateUsers(users, username));
    }

    @DeleteMapping(value = "/delete/{username}")
    public ResponseEntity<String> deleteCourse(@PathVariable String username){
        userService.deleteUsersByUsername(username);
        return ResponseEntity.ok().body("User with username: " + username + " successfully deleted");
    }
}