package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.OneTimePassword;

import java.util.Optional;

public interface OTPService {

    OneTimePassword createOTP(String email);

    Optional<OneTimePassword> findByOtp(String otp);

    OneTimePassword verifyExpiration(OneTimePassword otp);

    void deleteByEmail(String email);
}