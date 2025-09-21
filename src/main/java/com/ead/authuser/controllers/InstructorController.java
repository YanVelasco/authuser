package com.ead.authuser.controllers;

import com.ead.authuser.dtos.InstructorDto;
import com.ead.authuser.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

    final UserService userService;

    public InstructorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(
            @RequestBody @Valid InstructorDto instructorDto
    ) {
        userService.saveSubscriptionInstructor(userService.getUserById(instructorDto.userId()));
        return ResponseEntity.status(HttpStatus.OK).body(userService.saveSubscriptionInstructor(userService.getUserById(instructorDto.userId())));
    }
}
