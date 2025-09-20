package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CoursePageDto;
import com.ead.authuser.dtos.UserCourseDto;
import com.ead.authuser.service.UserCourseService;
import com.ead.authuser.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserCourseController {

    final CourseClient courseClient;
    final UserService userService;
    final UserCourseService userCourseService;

    public UserCourseController(CourseClient courseClient, UserService userService,
                                UserCourseService userCourseService) {
        this.courseClient = courseClient;
        this.userService = userService;
        this.userCourseService = userCourseService;
    }

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<CoursePageDto> getCoursesByUser(
            @PageableDefault(sort = "courseId", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable(value = "userId") UUID userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId, pageable));
    }

    @PostMapping("/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Valid UserCourseDto userCourseDto
    ) {
        var userModel = userService.getUserById(userId);
        if (userCourseService.existsByUserAndCourseId(userModel, userCourseDto.courseId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseService.save(userModel.convertToUserCourseModel(userCourseDto.courseId())));
    }

}
