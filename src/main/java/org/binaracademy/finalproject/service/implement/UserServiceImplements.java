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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
    private ResetPasswordService resetPasswordService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserResponse toUserResponse(Users users) {
        return UserResponse.builder()
                .fullname(users.getFullName())
                .email(users.getEmail())
                .password(users.getPassword())
                .phoneNumber(users.getPhoneNumber())
                .country(users.getCountry())
                .city(users.getCity())
                .linkPhoto(users.getLinkPhoto())
                .isActive(users.getIsActive())
                .build();
    }

    private GetUserResponse getUserResponse(Users users) {
        return GetUserResponse.builder()
                .id(users.getId())
                .password(users.getPassword())
                .fullname(users.getFullName())
                .email(users.getEmail())
                .phoneNumber(users.getPhoneNumber())
                .country(users.getCountry())
                .city(users.getCity())
                .linkPhoto(users.getLinkPhoto())
                .isActive(users.getIsActive())
                .build();
    }

    @Override
    public UserResponse updateUsers(UpdateUserRequest updateUsers) {
        try {
            log.info("Process updating user");
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Users users = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            users.setFullName(updateUsers.getFullName() == null ? users.getFullName() : updateUsers.getFullName());
            users.setPhoneNumber(updateUsers.getPhoneNumber() == null ? users.getPhoneNumber() : updateUsers.getPhoneNumber());
            users.setCountry(updateUsers.getCountry() == null ? users.getCountry() : updateUsers.getCountry());
            users.setCity(updateUsers.getCity() == null ? users.getCity() : updateUsers.getCity());
            userRepository.save(users);
            log.info("Updating user successful!");

            return toUserResponse(users);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteUsersByEmail() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Users users = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

            resetPasswordService.deleteByEmail(users.getEmail());
            otpService.deleteByEmail(users.getEmail());
            orderService.deleteByEmail(users.getEmail());
            users.getRoles().clear();

            userRepository.deleteUserFromEmail(users.getEmail());
            log.info("Successfully deleted user!");
        } catch (Exception e){
            log.error("Error: " + e.getMessage());
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
        try {
            log.info("Getting information details from user!");
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Users users = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

            GetUserResponse getUserResponse = new GetUserResponse();
            getUserResponse.setId(users.getId());
            getUserResponse.setFullname(users.getFullName());
            getUserResponse.setEmail(users.getEmail());
            getUserResponse.setPassword(users.getPassword());
            getUserResponse.setPhoneNumber(users.getPhoneNumber());
            getUserResponse.setCountry(users.getCountry());
            getUserResponse.setCity(users.getCity());
            getUserResponse.setLinkPhoto(users.getLinkPhoto());
            getUserResponse.setIsActive(users.getIsActive());

            return getUserResponse;
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteUserForAdmin(String email) {
        log.info("Trying to deleting user with email: {}", email);
        try {
            Users users = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

            resetPasswordService.deleteByEmail(email);
            otpService.deleteByEmail(email);
            orderService.deleteByEmail(email);
            users.getRoles().clear();

            userRepository.deleteUserFromEmail(email);
            log.info("Successfully deleted user {}!", email);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Users users = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

            log.info("Trying to change password for user: {}", users.getFullName());
            if (!passwordEncoder.matches(request.getCurrentPassword(), users.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Password");
            }
            if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Password");
            }

            users.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(users);
            log.info("Successfully changed password!");
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }
}