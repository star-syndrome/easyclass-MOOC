package org.binaracademy.finalproject.service;

import org.binaracademy.finalproject.model.request.ChangePasswordRequest;
import org.binaracademy.finalproject.model.response.GetUserResponse;
import org.binaracademy.finalproject.model.response.UserResponse;
import org.binaracademy.finalproject.model.request.UpdateUserRequest;

import java.util.List;

public interface UserService {

    UserResponse updateUsers(UpdateUserRequest updateUsers);

    void deleteUsersByEmail();

    List<GetUserResponse> getAllUser();

    GetUserResponse getUser();

    void deleteUserForAdmin(String email);

    void changePassword(ChangePasswordRequest request);
}