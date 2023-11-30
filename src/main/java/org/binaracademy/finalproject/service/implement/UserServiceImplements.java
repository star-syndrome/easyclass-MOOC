package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.response.UserResponse;
import org.binaracademy.finalproject.model.request.UpdateUserRequest;
import org.binaracademy.finalproject.model.Users;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional
public class UserServiceImplements implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private UserResponse toUserResponse(Users users) {
        return UserResponse.builder()
                .username(users.getUsername())
                .email(users.getEmail())
                .password(users.getPassword())
                .phoneNumber(users.getPhoneNumber())
                .country(users.getCountry())
                .city(users.getCity())
                .linkProfilePicture(users.getLinkProfilePicture())
                .build();
    }

    @Override
    public UserResponse updateUsers(UpdateUserRequest users, String username) {
        log.info("Process updating user");
        Users users1 = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        users1.setUsername(users.getUsername());
        users1.setPassword(passwordEncoder.encode(users.getPassword()));
        users1.setEmail(users.getEmail());
        users1.setPhoneNumber(users.getPhoneNumber());
        users1.setCountry(users.getCountry());
        users1.setCity(users.getCity());
        users1.setLinkProfilePicture(users.getLinkProfilePicture());
        userRepository.save(users1);
        log.info("Updating user with username: " + username + " successful!");

        return toUserResponse(users1);
    }

    @Override
    public void deleteUsersByUsername(String username) {
        try {
            Users users = userRepository.findByUsername(username).orElse(null);
            if (!Optional.ofNullable(users).isPresent()){
                log.info("User is not available");
            }
            assert users != null;
            users.getRoles().clear();
            userRepository.deleteUserFromUsername(username);
            log.info("Successfully deleted user!");
        } catch (Exception e){
            log.error("Deleting user failed, please try again!");
        }
    }
}