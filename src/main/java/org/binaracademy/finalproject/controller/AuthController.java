package org.binaracademy.finalproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.OneTimePassword;
import org.binaracademy.finalproject.model.ResetPassword;
import org.binaracademy.finalproject.model.request.OTPRequest;
import org.binaracademy.finalproject.model.request.ResetPasswordRequest;
import org.binaracademy.finalproject.model.response.ResponseController;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.security.config.JwtUtils;
import org.binaracademy.finalproject.security.request.LoginRequest;
import org.binaracademy.finalproject.security.request.SignupRequest;
import org.binaracademy.finalproject.security.response.JwtResponseOTP;
import org.binaracademy.finalproject.security.response.JwtResponseSignIn;
import org.binaracademy.finalproject.security.response.MessageResponse;
import org.binaracademy.finalproject.service.AuthService;
import org.binaracademy.finalproject.service.OTPService;
import org.binaracademy.finalproject.service.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

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

    @Autowired
    private ResetPasswordService resetPasswordService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<JwtResponseSignIn> authenticateUser(@Valid @RequestBody LoginRequest login) {
        return ResponseEntity.ok()
                .body(authService.authenticateUser(login));
    }

    @PostMapping("/register")
    @Operation(summary = "Register account for user")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok()
                .body(authService.registerUser(signupRequest));
    }

    @PostMapping("/otp")
    @Operation(summary = "For verify OTP")
    public ResponseEntity<?> otpVerify(@Valid @RequestBody OTPRequest otpRequest) {
        try {
            String oneTimePassword = otpRequest.getOtp();
            return otpService.findByOtp(oneTimePassword)
                    .map(otpService::verifyExpiration)
                    .map(OneTimePassword::getUsers)
                    .map(users -> {
                        String jwt = jwtUtils.generateTokenFromUsername(users.getUsername());
                        return ResponseEntity.ok(new JwtResponseOTP(jwt, users.getId(), users.getUsername(), users.getEmail()));
                    })
                    .orElseThrow(() -> new RuntimeException("OTP different! Please check your OTP correctly"));
        } catch (RuntimeException rte) {
            return ResponseController.statusResponse(HttpStatus.BAD_REQUEST, rte.getMessage(), null);
        }
    }

    @PostMapping("/refreshOTP")
    public ResponseEntity<MessageResponse> refreshOTP(@RequestParam String email) {
        return ResponseEntity.ok()
                .body(authService.refreshOTP(email));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordRequest request, @RequestParam String token) {
        try {
            resetPasswordService.findByToken(token)
                    .map(resetPasswordService::verifyExpiration)
                    .map(ResetPassword::getUsers)
                    .ifPresent(user -> {
                        if (!Objects.equals(request.getNewPassword(), request.getConfirmationPassword())) {
                            throw new RuntimeException("Wrong Password");
                        }
                        user.setPassword(passwordEncoder.encode(request.getConfirmationPassword()));
                        userRepository.save(user);
                    });
            return ResponseController.statusResponse(HttpStatus.OK, "Success reset password!", null);
        } catch (RuntimeException rte) {
            return ResponseController.statusResponse(HttpStatus.NOT_FOUND, rte.getMessage(), null);
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }

    @PostMapping("/sendToken")
    @Operation(summary = "Send token for reset password")
    public ResponseEntity<Object> sendToken(@RequestParam String username) {
        try {
            return ResponseController.statusResponse(HttpStatus.OK,
                    "Success send a token!", authService.sendToken(username));
        } catch (Exception e) {
            return ResponseController.internalServerError(e.getMessage());
        }
    }
}