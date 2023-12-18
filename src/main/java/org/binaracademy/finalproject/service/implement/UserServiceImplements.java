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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private OTPService otpService;

    private UserResponse toUserResponse(Users users) {
        return UserResponse.builder()
                .username(users.getUsername())
                .email(users.getEmail())
                .password(users.getPassword())
                .phoneNumber(users.getPhoneNumber())
                .country(users.getCountry())
                .city(users.getCity())
                .linkPhoto(users.getLinkPhoto())
                .build();
    }

    private GetUserResponse getUserResponse(Users users) {
        return GetUserResponse.builder()
                .id(users.getId())
                .password(users.getPassword())
                .username(users.getUsername())
                .email(users.getEmail())
                .phoneNumber(users.getPhoneNumber())
                .country(users.getCountry())
                .city(users.getCity())
                .linkPhoto(users.getLinkPhoto())
                .build();
    }

    @Override
    public UserResponse updateUsers(UpdateUserRequest updateUsers) {
        try {
            log.info("Process updating user");
            String username = getAuth();
            Optional<Users> users = Optional.ofNullable(userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found")));
            Users users1 = users.get();

            users1.setPhoneNumber(updateUsers.getPhoneNumber() == null ? users1.getPhoneNumber() : updateUsers.getPhoneNumber());
            users1.setCountry(updateUsers.getCountry() == null ? users1.getCountry() : updateUsers.getCountry());
            users1.setCity(updateUsers.getCity() == null ? users1.getCity() : updateUsers.getCity());
            userRepository.save(users1);
            log.info("Updating user successful!");

            return toUserResponse(users1);
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

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser() {
        log.info("Getting information details from user!");
        String username = getAuth();
        Optional<Users> users = userRepository.findByUsername(username);
        Users users1 = users.get();

        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(users1.getUsername());
        userResponse.setEmail(users1.getEmail());
        userResponse.setPassword(users1.getPassword());
        userResponse.setPhoneNumber(users1.getPhoneNumber());
        userResponse.setCountry(users1.getCountry());
        userResponse.setCity(users1.getCity());
        userResponse.setLinkPhoto(users1.getLinkPhoto());

        return userResponse;
    }

    private String getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return username;
    }
}