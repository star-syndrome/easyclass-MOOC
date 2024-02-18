package org.binaracademy.finalproject.repository;

import org.binaracademy.finalproject.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Modifying
    void deleteByEmail(String email);
}