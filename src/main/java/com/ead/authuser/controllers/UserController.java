package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.dtos.UserPageDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserPageDto> getUsers(
            Pageable pageable,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) UserStatus userStatus,
            @RequestParam(required = false) UserType userType,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UUID courseId
    ) {
        logger.debug("GET getUsers: fullName {}, userStatus {}, userType {}, username {}, email {}, courseId {}",
                fullName, userStatus, userType, username, email, courseId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findAll(pageable, fullName, userStatus, userType, username, email, courseId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(
            @PathVariable(value = "userId") UUID userId
    ) {
        logger.debug("GET getUserById: userId {}", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(
            @PathVariable(value = "userId") UUID userId
    ) {
        logger.debug("DELETE deleteUserById: userId {}", userId);
        userService.deleteUserById(userService.getUserById(userId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUserById(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Validated(UserDto.UserView.UserPut.class)
            @JsonView(UserDto.UserView.UserPut.class) UserDto userDto
    ) {
        logger.debug("PUT updateUserById: userId {}, userDto {}", userId, userDto);
        UserModel userModel = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userModel, userDto));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updateUserPassword(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Validated(UserDto.UserView.PasswordPut.class)
            @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto
    ) {
        logger.debug("PUT updateUserPassword: userId {}, userDto {}", userId, userDto);
        var userModel = userService.getUserById(userId);
        if (!userModel.getPassword().equals(userDto.oldPassword())) {
            logger.warn("Put updateUserPassword: old password doesn't match for this user {}", userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password does not match.");
        }
        logger.debug("PUT updateUserPassword: userId {}, userDto {}", userId, userDto);
        userService.updatePassword(userModel, userDto);
        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateUserImage(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Validated(UserDto.UserView.ImagePut.class) @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto
    ) {
        logger.debug("PUT updateUserImage: userId {}, userDto {}", userId, userDto);
        UserModel userModel = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserImage(userModel, userDto));
    }

}