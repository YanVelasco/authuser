package com.ead.authuser.service;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.dtos.UserPageDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserModel> getAllUsers();

    UserModel getUserById(UUID userId);

    void deleteUserById( UserModel userModel);

    UserModel registerUser(UserDto userDto);

    UserModel updateUser(UserModel userModel, UserDto userDto);

    UserModel updatePassword(UserModel userModel, UserDto userDto);

    UserModel updateUserImage(UserModel userModel, UserDto userDto);

    UserPageDto findAll(Pageable pageable, String fullName, UserStatus userStatus, UserType userType, String username, String email, UUID courseId);

}
