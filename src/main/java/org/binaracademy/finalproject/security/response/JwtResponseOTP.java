package org.binaracademy.finalproject.security.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseOTP {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;

    public JwtResponseOTP(String token, Long id, String username, String email) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
    }
}