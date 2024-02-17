package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.request.ResetPasswordRequest;
import org.binaracademy.finalproject.security.request.LoginRequest;
import org.binaracademy.finalproject.security.request.SignupRequest;
import org.binaracademy.finalproject.security.response.JwtResponse;
import org.binaracademy.finalproject.security.response.MessageResponse;


public interface AuthService {

    MessageResponse registerUser(SignupRequest signupRequest);

    JwtResponse authenticateUser(LoginRequest login);

    MessageResponse sendToken(String email);

    MessageResponse refreshOTP(String email);

    void verifyAccountWithOTP(String email, String otp);

    void resetPassword(ResetPasswordRequest request, String token);
}