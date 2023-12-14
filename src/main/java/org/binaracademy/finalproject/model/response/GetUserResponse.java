package org.binaracademy.finalproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponse {

    private Long id;
    private String password;
    private String username;
    private String email;
    private String phoneNumber;
    private String country;
    private String city;
}