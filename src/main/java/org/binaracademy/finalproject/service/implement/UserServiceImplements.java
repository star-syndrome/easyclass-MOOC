package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.response.GetUserResponse;
import org.binaracademy.finalproject.model.response.UserResponse;
import org.binaracademy.finalproject.model.request.UpdateUserRequest;
import org.binaracademy.finalproject.model.Users;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.service.OTPService;
import org.binaracademy.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class UserServiceImplements implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OTPService otpService;

    private UserResponse toUserResponse(Users users) {
        return UserResponse.builder()
                .username(users.getUsername())
                .email(users.getEmail())
                .password(users.getPassword())
                .phoneNumber(users.getPhoneNumber())
                .country(users.getCountry())
                .city(users.getCity())
                .build();
    }

    private GetUserResponse getUserResponse(Users users) {
        return GetUserResponse.builder()
                .username(users.getUsername())
                .email(users.getEmail())
                .phoneNumber(users.getPhoneNumber())
                .country(users.getCountry())
                .city(users.getCity())
                .build();
    }

    @Override
    public UserResponse updateUsers(UpdateUserRequest updateUsers, String username) {
        try {
            log.info("Process updating user");
            Users users = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            users.setUsername(updateUsers.getUsername() == null ? users.getUsername() : updateUsers.getUsername());
            users.setPassword(passwordEncoder.encode(updateUsers.getPassword() == null ? users.getPassword() : updateUsers.getPassword()));
            users.setEmail(updateUsers.getEmail() == null ? users.getEmail() : updateUsers.getEmail());
            users.setPhoneNumber(updateUsers.getPhoneNumber() == null ? users.getPhoneNumber() : updateUsers.getPhoneNumber());
            users.setCountry(updateUsers.getCountry() == null ? users.getCountry() : updateUsers.getCountry());
            users.setCity(updateUsers.getCity() == null ? users.getCity() : updateUsers.getCity());
            userRepository.save(users);
            log.info("Updating user with username: " + username + " successful!");

            return toUserResponse(users);
        } catch (Exception e) {
            log.error("Update user failed");
            throw e;
        }
    }

    @Override
    public void deleteUsersByUsername(String username) {
        try {
            Users users = userRepository.findByUsername(username).orElse(null);
            if (!Optional.ofNullable(users).isPresent()){
                log.info("User is not available");
            }
            otpService.deleteByUsername(username);
            assert users != null;
            users.getRoles().clear();
            userRepository.deleteUserFromUsername(username);
            log.info("Successfully deleted user!");
        } catch (Exception e){
            log.error("Deleting user failed, please try again!");
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetUserResponse> getAllUser() {
        log.info("Getting all of list user");
        return userRepository.findAll().stream()
                .map(this::getUserResponse)
                .collect(Collectors.toList());
    }
}