package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(
            @PathVariable(value = "userId") UUID userId
    ) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(
            @PathVariable(value = "userId") UUID userId
    ) {
        userService.deleteUserById(userService.getUserById(userId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUserById(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @JsonView(UserDto.UserView.UserPut.class) UserDto userDto
    ) {
        UserModel userModel = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userModel, userDto));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updateUserPassword(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto
    ) {
        var userModel = userService.getUserById(userId);
        if(!userModel.getPassword().equals(userDto.oldPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password does not match.");
        }
        userService.updatePassword(userModel, userDto);
        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateUserImage(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto
    ) {
        UserModel userModel = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserImage(userModel, userDto));
    }
}
