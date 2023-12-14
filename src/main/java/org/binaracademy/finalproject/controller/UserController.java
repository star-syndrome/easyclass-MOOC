package org.binaracademy.finalproject.controller;

import lombok.extern.slf4j.Slf4j;
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

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserLogin(){
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success get user!",
                    userService.getUser());
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @PutMapping(value = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUser(@RequestBody UpdateUserRequest users) {
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Update user successful!", userService.updateUsers(users));
        } catch (RuntimeException runtimeException) {
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    runtimeException.getMessage(), null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable String username){
        try {
            userService.deleteUsersByUsername(username);
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Deleting user successful!", null);
        } catch (RuntimeException runtimeException) {
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND,
                    runtimeException.getMessage(), null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<MessageResponse> uploadImage(@ModelAttribute UploadImageRequest uploadImageRequest) {
        log.info("uploader name : {}", uploadImageRequest.getUploaderName());
        log.info("file name : {}", uploadImageRequest.getFileName());
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
}