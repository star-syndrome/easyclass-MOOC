package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.ResetPassword;

import java.util.Optional;

public interface ResetPasswordService {

    ResetPassword createToken(String username);

    Optional<ResetPassword> findByToken(String token);

    ResetPassword verifyExpiration(ResetPassword token);

    void deleteByUsername(String username);
}