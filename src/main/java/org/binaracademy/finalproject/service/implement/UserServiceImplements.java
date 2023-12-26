package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.request.ChangePasswordRequest;
import org.binaracademy.finalproject.model.response.GetUserResponse;
import org.binaracademy.finalproject.model.response.UserResponse;
import org.binaracademy.finalproject.model.request.UpdateUserRequest;
import org.binaracademy.finalproject.model.Users;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.service.OTPService;
import org.binaracademy.finalproject.service.OrderService;
import org.binaracademy.finalproject.service.ResetPasswordService;
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

    @Autowired
    private OrderService orderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ResetPasswordService resetPasswordService;

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
    public void deleteUsersByUsername() {
        try {
            String user = getAuth();
            Optional<Users> users = userRepository.findByUsername(user);
            Users users1 = users.get();

            resetPasswordService.deleteByUsername(users1.getUsername());
            otpService.deleteByUsername(users1.getUsername());
            orderService.deleteByUsername(users1.getUsername());
            users1.getRoles().clear();
            userRepository.deleteUserFromUsername(users1.getUsername());
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
    public GetUserResponse getUser() {
        log.info("Getting information details from user!");
        String username = getAuth();
        Optional<Users> users = userRepository.findByUsername(username);
        Users users1 = users.get();

        GetUserResponse getUserResponse = new GetUserResponse();
        getUserResponse.setId(users1.getId());
        getUserResponse.setUsername(users1.getUsername());
        getUserResponse.setEmail(users1.getEmail());
        getUserResponse.setPassword(users1.getPassword());
        getUserResponse.setPhoneNumber(users1.getPhoneNumber());
        getUserResponse.setCountry(users1.getCountry());
        getUserResponse.setCity(users1.getCity());
        getUserResponse.setLinkPhoto(users1.getLinkPhoto());

        return getUserResponse;
    }

    @Override
    public void deleteUserForAdmin(String username) {
        log.info("Trying to deleting user with username: {}", username);
        try {
            Users users = userRepository.findByUsername(username).orElse(null);
            if (!Optional.ofNullable(users).isPresent()) {
                log.info("User is not available");
            }
            resetPasswordService.deleteByUsername(username);
            otpService.deleteByUsername(username);
            orderService.deleteByUsername(username);
            assert users != null;
            users.getRoles().clear();
            userRepository.deleteUserFromUsername(username);
            log.info("Successfully deleted user!");
        } catch (Exception e) {
            log.error("Deleting user failed, please try again!");
            throw e;
        }
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        String username = getAuth();
        Optional<Users> users = userRepository.findByUsername(username);
        Users users1 = users.get();

        log.info("Trying to change password for user: {}", users1.getUsername());
        if (!passwordEncoder.matches(request.getCurrentPassword(), users1.getPassword())) {
            throw new IllegalStateException("Wrong Password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Wrong Password");
        }
        users1.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(users1);
        log.info("Successfully changed password!");
    }

    private String getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return username;
    }
}