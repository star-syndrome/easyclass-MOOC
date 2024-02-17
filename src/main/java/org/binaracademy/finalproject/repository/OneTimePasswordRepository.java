package org.binaracademy.finalproject.repository;

import org.binaracademy.finalproject.model.OneTimePassword;
import org.binaracademy.finalproject.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Long> {

    Optional<OneTimePassword> findByOtp(String otp);

    OneTimePassword findByUsersId(Long id);

    Boolean existsByOtp(String otp);

    @Modifying
    void deleteByUsers(Users users);
}