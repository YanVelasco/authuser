package com.ead.authuser.validations;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    Logger logger = LogManager.getLogger(UserValidator.class);

    final Validator validator;
    final UserService userService;

    public UserValidator(@Qualifier("defaultValidator") Validator validator, UserService userService) {
        this.validator = validator;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto user = (UserDto) target;
        validator.validate(target, errors);
        if (!errors.hasErrors()) {
            logger.debug("Validating user: {}", user);
            validateUserName(user.username(), errors);
            validateEmail(user.email(), errors);
        }
    }

    public void validateUserName(String username, Errors errors) {
        if (userService.existsByUsername(username)) {
            errors.rejectValue("username", "409", "Username already exists");
            logger.error("Error on validate username: {}", username);
        }
    }

    public void validateEmail(String email, Errors errors) {
        if (userService.existsByEmail(email)) {
            errors.rejectValue("email", "409", "Email already exists");
            logger.error("Error on validate email: {}", email);
        }
    }

}
