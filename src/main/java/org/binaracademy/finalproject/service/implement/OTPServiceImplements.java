package org.binaracademy.finalproject.service.implement;

import org.apache.commons.lang3.RandomStringUtils;
import org.binaracademy.finalproject.model.OneTimePassword;
import org.binaracademy.finalproject.model.Users;
import org.binaracademy.finalproject.repository.OneTimePasswordRepository;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@Service
public class OTPServiceImplements implements OTPService {

    @Value("${jwt.ExpirationMs}")
    private Long otpDurationMs;

    @Autowired
    private OneTimePasswordRepository oneTimePasswordRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<OneTimePassword> findByOtp(String otp) {
        return oneTimePasswordRepository.findByOtp(otp);
    }

    @Override
    public OneTimePassword createOTP(String email) {
        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        OneTimePassword oneTimePassword = oneTimePasswordRepository.findByUsersId(users.getId());

        if (oneTimePassword == null) {
            oneTimePassword = new OneTimePassword();
        }

        oneTimePassword.setUsers(users);
        oneTimePassword.setExpiryDate(Instant.now().plusMillis(otpDurationMs));
        oneTimePassword.setOtp(RandomStringUtils.randomNumeric(6));

        oneTimePasswordRepository.save(oneTimePassword);
        return oneTimePassword;
    }

    @Override
    public OneTimePassword verifyExpiration(OneTimePassword otp) {
        if (otp.getExpiryDate().compareTo(Instant.now()) < 0) {
            oneTimePasswordRepository.delete(otp);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP was expired!");
        }
        return otp;
    }

    @Override
    @Transactional
    public void deleteByEmail(String email) {
        oneTimePasswordRepository.deleteByUsers(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")));
    }
}