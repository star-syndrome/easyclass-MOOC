package org.binaracademy.finalproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.request.ChangePasswordRequest;
import org.binaracademy.finalproject.model.request.UploadImageRequest;
import org.binaracademy.finalproject.model.response.ResponseController;
import org.binaracademy.finalproject.model.request.UpdateUserRequest;
import org.binaracademy.finalproject.security.response.MessageResponse;
import org.binaracademy.finalproject.service.CloudinaryService;
import org.binaracademy.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getUserLogin(){
        return ResponseController.statusResponse(HttpStatus.OK, "Success get user!", userService.getUser());
    }

    @PutMapping(value = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUser(@Validated @RequestBody UpdateUserRequest users) {
        return ResponseController.statusResponse(HttpStatus.OK,
                "Update user successful!", userService.updateUsers(users));
    }

    @DeleteMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> deleteUser(){
        userService.deleteUsersByEmail();
        return ResponseController.statusResponse(HttpStatus.OK, "Deleting user successful!", null);
    }

    @PostMapping(path = "/upload-image")
    public ResponseEntity<MessageResponse> uploadImage(@ModelAttribute UploadImageRequest uploadImageRequest) {
        log.info("Trying to upload profile picture!");
        return Optional.of(uploadImageRequest)
                .map(UploadImageRequest::getMultipartFile)
                .filter(file -> !file.isEmpty())
                .map(file -> {
                    try {
                        return new ResponseEntity<>(cloudinaryService.upload(file), HttpStatus.OK);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElse(new ResponseEntity<>(MessageResponse.builder()
                        .message("Upload image failed")
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PatchMapping(
            path = "/change-password",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> changePassword(@Validated @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        return ResponseController.statusResponse(HttpStatus.OK, "Change Password successful!", null);
    }
}