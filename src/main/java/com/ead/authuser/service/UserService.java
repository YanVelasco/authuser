package com.ead.authuser.service;

import com.ead.authuser.models.UserModel;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserModel> getAllUsers();

    UserModel getUserById(UUID userId);

    void deleteUserById( UserModel userModel);

}
