package org.binaracademy.finalproject.repository;

import org.binaracademy.finalproject.model.ResetPassword;
import org.binaracademy.finalproject.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {

    Optional<ResetPassword> findByToken(String token);

    ResetPassword findByUsersId(Long id);

    Boolean existsByToken(String token);

    @Modifying
    void deleteByUsers(Users users);
}