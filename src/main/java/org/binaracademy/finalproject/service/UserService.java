package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.response.UserResponse;
import org.binaracademy.finalproject.model.request.UpdateUserRequest;

public interface UserService {

    UserResponse updateUsers(UpdateUserRequest users, String username);

    void deleteUsersByUsername(String username);
}