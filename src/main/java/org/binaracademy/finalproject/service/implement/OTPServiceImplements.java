package org.binaracademy.finalproject.service.implement;

import org.apache.commons.lang3.RandomStringUtils;
import org.binaracademy.finalproject.model.OneTimePassword;
import org.binaracademy.finalproject.repository.OneTimePasswordRepository;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public OneTimePassword createOTP(String username) {
        OneTimePassword oneTimePassword = new OneTimePassword();

        oneTimePassword.setUsers(userRepository.getUserByUsername(username).get());
        oneTimePassword.setExpiryDate(Instant.now().plusMillis(otpDurationMs));
        oneTimePassword.setOtp(RandomStringUtils.randomNumeric(6));

        oneTimePassword = oneTimePasswordRepository.save(oneTimePassword);
        return oneTimePassword;
    }

    @Override
    public OneTimePassword verifyExpiration(OneTimePassword otp) {
        if (otp.getExpiryDate().compareTo(Instant.now()) < 0) {
            oneTimePasswordRepository.delete(otp);
            throw new RuntimeException("OTP was expired");
        }
        return otp;
    }

    @Transactional
    @Override
    public void deleteByUsername(String username) {
        oneTimePasswordRepository.deleteByUsers(userRepository.getUserByUsername(username).get());
    }
}