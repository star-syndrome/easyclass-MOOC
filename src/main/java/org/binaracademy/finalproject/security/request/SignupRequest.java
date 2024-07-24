package org.binaracademy.finalproject.security.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotBlank
    @Size(min = 10, max = 25)
    private String fullName;

    @NotBlank
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    private String password;

    @Size(max = 15)
    private String phoneNumber;

    @Size(max = 50)
    private String country;

    @Size(max = 50)
    private String city;
}