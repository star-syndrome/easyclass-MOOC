package org.binaracademy.finalproject.controller;

import org.binaracademy.finalproject.model.request.ResetPasswordRequest;
import org.binaracademy.finalproject.model.response.ResponseController;
import org.binaracademy.finalproject.security.request.LoginRequest;
import org.binaracademy.finalproject.security.request.SignupRequest;
import org.binaracademy.finalproject.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
            path = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> authenticateUser(@Validated @RequestBody LoginRequest login) {
        return ResponseController.statusResponse(HttpStatus.OK,
                "Success login!", authService.authenticateUser(login));
    }

    @PostMapping(
            path = "/register",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> registerUser(@Validated @RequestBody SignupRequest signupRequest) {
        return ResponseController.statusResponse(HttpStatus.OK,
                "Success register account!", authService.registerUser(signupRequest));
    }

    @PostMapping(
            path = "/verifyOTP",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> otpVerify(@RequestParam String email, @RequestParam String otp) {
        authService.verifyAccountWithOTP(email, otp);
        return ResponseController.statusResponse(HttpStatus.OK, "Success verify account!", null);
    }

    @PostMapping(
            path = "/refreshOTP",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> refreshOTP(@RequestParam String email) {
        return ResponseController.statusResponse(HttpStatus.OK,
                "Success refresh OTP!", authService.refreshOTP(email));
    }

    @PostMapping(
            path = "/verifyResetPassword",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordRequest request, @RequestParam String token) {
        authService.resetPassword(request, token);
        return ResponseController.statusResponse(HttpStatus.OK, "Success reset password!", null);
    }

    @PostMapping(
            path = "/sendTokenForResetPassword",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> sendToken(@RequestParam String email) {
        return ResponseController.statusResponse(HttpStatus.OK,
                "Success send a token!", authService.sendToken(email));
    }
}