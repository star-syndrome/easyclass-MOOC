package org.binaracademy.finalproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.OneTimePassword;
import org.binaracademy.finalproject.model.request.OTPRequest;
import org.binaracademy.finalproject.security.config.JwtUtils;
import org.binaracademy.finalproject.security.request.LoginRequest;
import org.binaracademy.finalproject.security.request.SignupRequest;
import org.binaracademy.finalproject.security.response.JwtResponseOTP;
import org.binaracademy.finalproject.security.response.JwtResponseSignIn;
import org.binaracademy.finalproject.security.response.MessageResponse;
import org.binaracademy.finalproject.service.AuthService;
import org.binaracademy.finalproject.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OTPService otpService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponseSignIn> authenticateUser(@Valid @RequestBody LoginRequest login) {
        return ResponseEntity.ok()
                .body(authService.authenticateUser(login));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok()
                .body(authService.registerUser(signupRequest));
    }

    @PostMapping("/otp")
    public ResponseEntity<JwtResponseOTP> otpVerify(@Valid @RequestBody OTPRequest otpRequest) {
        String oneTimePassword = otpRequest.getOtp();
        return otpService.findByOtp(oneTimePassword)
                .map(otpService::verifyExpiration)
                .map(OneTimePassword::getUsers)
                .map(users -> {
                    String jwt = jwtUtils.generateTokenFromUsername(users.getUsername());
                    return ResponseEntity.ok(new JwtResponseOTP(jwt, users.getId(), users.getUsername(), users.getEmail()));
                })
                .orElseThrow(() -> new RuntimeException("OTP different! Please check your OTP correctly"));
    }
}