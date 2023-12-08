package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.OneTimePassword;

import java.util.Optional;

public interface OTPService {

    OneTimePassword createOTP(String username);

    Optional<OneTimePassword> findByOtp(String otp);

    OneTimePassword verifyExpiration(OneTimePassword otp);

    void deleteByUsername(String username);
}