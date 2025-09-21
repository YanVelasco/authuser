package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.service.UserService;
import com.ead.authuser.validations.UserValidator;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger logger = LogManager.getLogger(AuthenticationController.class);

    final UserService userService;
    final UserValidator userValidator;

    public AuthenticationController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(
            @RequestBody @Validated(UserDto.UserView.RegistrationPost.class) @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto,
            Errors errors
    ) {
        logger.debug("Post registerUser: {}", userDto);
        userValidator.validate(userDto, errors);
        if (errors.hasErrors()) {
            logger.error("Error on register user: {}", errors.getAllErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userDto));
    }

}
