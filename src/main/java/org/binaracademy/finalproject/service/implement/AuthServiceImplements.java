package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.OneTimePassword;
import org.binaracademy.finalproject.model.ResetPassword;
import org.binaracademy.finalproject.model.Roles;
import org.binaracademy.finalproject.model.Users;
import org.binaracademy.finalproject.model.request.EmailRequest;
import org.binaracademy.finalproject.model.request.ResetPasswordRequest;
import org.binaracademy.finalproject.repository.OneTimePasswordRepository;
import org.binaracademy.finalproject.repository.ResetPasswordRepository;
import org.binaracademy.finalproject.repository.RoleRepository;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.security.config.JwtUtils;
import org.binaracademy.finalproject.security.enumeration.ERole;
import org.binaracademy.finalproject.security.request.LoginRequest;
import org.binaracademy.finalproject.security.request.SignupRequest;
import org.binaracademy.finalproject.security.response.JwtResponse;
import org.binaracademy.finalproject.security.response.MessageResponse;
import org.binaracademy.finalproject.service.AuthService;
import org.binaracademy.finalproject.service.EmailService;
import org.binaracademy.finalproject.service.OTPService;
import org.binaracademy.finalproject.service.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class AuthServiceImplements implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private OTPService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ResetPasswordService resetPasswordService;

    @Autowired
    private OneTimePasswordRepository oneTimePasswordRepository;

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Autowired
    private UserRepository userRepository;

    public AuthServiceImplements(AuthenticationManager authenticationManager, UserRepository usersRepository,
                                 JwtUtils jwtUtils, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public MessageResponse registerUser(SignupRequest signupRequest) {
        try {
            Boolean emailExist = usersRepository.findByEmail(signupRequest.getEmail()).isPresent();
            if (Boolean.TRUE.equals(emailExist)) {
                return MessageResponse.builder()
                        .message("Error: Email is already taken!")
                        .build();
            }

            Users users = new Users();
            users.setFullName(signupRequest.getFullName());
            users.setEmail(signupRequest.getEmail());
            users.setIsActive(false);
            users.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            users.setPhoneNumber(signupRequest.getPhoneNumber());
            users.setCountry(signupRequest.getCountry());
            users.setCity(signupRequest.getCity());

            Set<String> strRoles = signupRequest.getRole();
            Set<Roles> roles = new HashSet<>();

            if (strRoles == null) {
                Roles role = roleRepository.findByRoleName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                roles.add(role);
            } else {
                strRoles.forEach(role -> {
                    Roles roles1 = roleRepository.findByRoleName(ERole.valueOf(role))
                            .orElseThrow(() -> new RuntimeException("Error: Role " + role + " is not found"));
                    roles.add(roles1);
                });
            }
            users.setRoles(roles);
            usersRepository.save(users);

            OneTimePassword oneTimePassword = otpService.createOTP(users.getEmail());
            emailService.sendEmail(EmailRequest.builder()
                    .subject("One Time Password")
                    .recipient(users.getEmail())
                    .content("Please input this OTP " + oneTimePassword.getOtp() +
                            " to verify your account for access Easy Class, thank you!")
                    .build());

            log.info("User registered successfully, name: {}", users.getFullName());
            return MessageResponse.builder()
                    .message("User registered successfully, name: " + users.getFullName())
                    .build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest login) {
        try {
            Users users = usersRepository.findByEmail(login.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

            if (!users.getIsActive()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account has not been verified!");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(users.getEmail(), login.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            Users userDetails = (Users) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            log.info("User: " + userDetails.getFullName() + " successfully sign in");
            return new JwtResponse(jwt, userDetails.getId(), userDetails.getFullName(),
                    userDetails.getEmail(), roles);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public MessageResponse sendToken(String email) {
        try {
            log.info("Trying send token to {} for reset password", email);
            Users users = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
            ResetPassword resetPassword = resetPasswordService.createToken(email);

            emailService.sendEmail(EmailRequest.builder()
                    .subject("Reset Password for Easy Class")
                    .recipient(users.getEmail())
                    .content("Click the link below to reset your password!" +
                            "\nhttps://easyclass-course.vercel.app/auth/resetPassword?token=" + resetPassword.getToken())
                    .build());
            log.info("Send token for reset password successfully!");
            return MessageResponse.builder().message("Success sending a token for reset password!").build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public MessageResponse refreshOTP(String email) {
        try {
            log.info("Trying to refresh OTP");
            Users users = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "User not found with this email: " + email));
            OneTimePassword otp = otpService.createOTP(users.getEmail());

            emailService.sendEmail(EmailRequest.builder()
                        .subject("Refresh OTP For Easy Class")
                        .recipient(users.getEmail())
                        .content(otp.getOtp())
                        .build());
            log.info("Refresh OTP success!");
            return MessageResponse.builder().message("Refresh OTP Success!").build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void verifyAccountWithOTP(String email, String otp) {
        try {
            log.info("Trying to verify account with OTP and email");
            if (!usersRepository.existsByEmail(email)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!");
            }

            if (!oneTimePasswordRepository.existsByOtp(otp)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP not match!");
            }

            otpService.findByOtp(otp)
                    .map(otpService::verifyExpiration)
                    .map(OneTimePassword::getUsers)
                    .ifPresent(users -> {
                        users.setIsActive(true);
                        usersRepository.save(users);
                        otpService.deleteByEmail(users.getEmail());
                    });
            log.info("Success verify account!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequest request, String token) {
        try {
            log.info("Trying to reset password");
            if (!resetPasswordRepository.existsByToken(token)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Link not valid!");
            }

            resetPasswordService.findByToken(token)
                    .map(resetPasswordService::verifyExpiration)
                    .map(ResetPassword::getUsers)
                    .ifPresent(users -> {
                        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password not match");
                        }
                        String encodePassword = passwordEncoder.encode(request.getNewPassword());
                        users.setPassword(encodePassword);
                        userRepository.save(users);
                        resetPasswordService.deleteByEmail(users.getEmail());
                    });
            log.info("Reset password successful!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }
}