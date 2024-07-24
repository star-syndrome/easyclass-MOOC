package org.binaracademy.finalproject.service.implement;

import org.apache.commons.lang3.RandomStringUtils;
import org.binaracademy.finalproject.model.ResetPassword;
import org.binaracademy.finalproject.model.Users;
import org.binaracademy.finalproject.repository.ResetPasswordRepository;
import org.binaracademy.finalproject.repository.UserRepository;
import org.binaracademy.finalproject.service.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@Service
public class ResetPasswordServiceImplements implements ResetPasswordService {

    @Value("${jwt.ExpirationMs}")
    private Long tokenDurationMs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Override
    public ResetPassword createToken(String email) {
        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        ResetPassword resetPassword = resetPasswordRepository.findByUsersId(users.getId());

        if (resetPassword == null) {
            resetPassword = new ResetPassword();
        }

        resetPassword.setUsers(users);
        resetPassword.setExpiryDate(Instant.now().plusMillis(tokenDurationMs));
        resetPassword.setToken(RandomStringUtils.randomNumeric(10));

        resetPasswordRepository.save(resetPassword);
        return resetPassword;
    }

    @Override
    public Optional<ResetPassword> findByToken(String token) {
        return resetPasswordRepository.findByToken(token);
    }

    @Override
    public ResetPassword verifyExpiration(ResetPassword token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            resetPasswordRepository.delete(token);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token was expired!");
        }
        return token;
    }

    @Override
    @Transactional
    public void deleteByEmail(String email) {
        resetPasswordRepository.deleteByUsers(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")));
    }
}