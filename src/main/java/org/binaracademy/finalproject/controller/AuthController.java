package org.binaracademy.finalproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.request.OTPRequest;
import org.binaracademy.finalproject.model.response.ResponseController;
import org.binaracademy.finalproject.security.request.LoginRequest;
import org.binaracademy.finalproject.security.request.SignupRequest;
import org.binaracademy.finalproject.security.response.JwtResponse;
import org.binaracademy.finalproject.security.response.MessageResponse;
import org.binaracademy.finalproject.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*")
@Slf4j
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest login) {
        return ResponseEntity.ok()
                .body(authService.authenticateUser(login));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok()
                .body(authService.registerUser(signupRequest));
    }

    @PostMapping("/otp")
    public ResponseEntity<Object> otpVerify(@Valid @RequestBody OTPRequest otpRequest) {
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "OTP Verify successful!", authService.otpVerify(otpRequest));
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }
}