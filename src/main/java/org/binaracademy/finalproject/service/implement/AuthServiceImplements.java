package org.binaracademy.finalproject.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.binaracademy.finalproject.model.OneTimePassword;
import org.binaracademy.finalproject.model.Roles;
import org.binaracademy.finalproject.model.Users;
import org.binaracademy.finalproject.model.request.EmailRequest;
import org.binaracademy.finalproject.model.request.OTPRequest;
import org.binaracademy.finalproject.repository.RoleRepository;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.security.UserDetailsImpl;
import org.binaracademy.finalproject.security.config.JwtUtils;
import org.binaracademy.finalproject.security.enumeration.ERole;
import org.binaracademy.finalproject.security.request.LoginRequest;
import org.binaracademy.finalproject.security.request.SignupRequest;
import org.binaracademy.finalproject.security.response.JwtResponse;
import org.binaracademy.finalproject.security.response.MessageResponse;
import org.binaracademy.finalproject.service.AuthService;
import org.binaracademy.finalproject.service.EmailService;
import org.binaracademy.finalproject.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        Boolean usernameExist = usersRepository.existsByUsername(signupRequest.getUsername());
        if(Boolean.TRUE.equals(usernameExist)) {
            return MessageResponse.builder()
                    .message("Error: Username is already taken!")
                    .build();
        }

        Boolean emailExist = usersRepository.existsByEmail(signupRequest.getEmail());
        if(Boolean.TRUE.equals(emailExist)) {
            return MessageResponse.builder()
                    .message("Error: Email is already taken!")
                    .build();
        }

        Users users = new Users(signupRequest.getUsername(), signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getPhoneNumber(),
                signupRequest.getCountry(), signupRequest.getCity());

        Set<String> strRoles = signupRequest.getRole();
        Set<Roles> roles = new HashSet<>();

        if(strRoles == null) {
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

        OneTimePassword oneTimePassword = otpService.createOTP(users.getUsername());
        emailService.sendEmail(EmailRequest.builder()
                        .subject("One Time Password")
                        .recipient(users.getEmail())
                        .content("Please insert this OTP " + oneTimePassword.getOtp() + " for verify your account to access Easy Class, thank you!")
                .build());

        log.info("User registered successfully, username: {}", users.getUsername());
        return MessageResponse.builder()
                .message("User registered successfully, username: " + users.getUsername())
                .build();
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest login) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        log.info("User: " + userDetails.getUsername() + " successfully sign in");
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    @Override
    public MessageResponse otpVerify(OTPRequest otpRequest) {
        String oneTimePassword = otpRequest.getOtp();
        otpService.findByOtp(oneTimePassword)
                .map(otpService::verifyExpiration)
                .map(OneTimePassword::getUsers)
                .map(users -> "Your account is verify!")
                .orElseThrow(() -> new RuntimeException("OTP different! Please check your OTP correctly"));
        return MessageResponse.builder()
                .message("Successfully verify OTP! Please sign in to access easyclass!")
                .build();
    }
}