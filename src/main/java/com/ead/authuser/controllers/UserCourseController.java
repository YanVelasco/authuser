package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.configs.security.UserDetailsImpl;
import com.ead.authuser.dtos.CoursePageDto;
import com.ead.authuser.service.UserService;
import com.ead.authuser.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@RestController
public class UserCourseController {

    final CourseClient courseClient;
    final UserService userService;

    public UserCourseController(CourseClient courseClient, UserService userService
    ) {
        this.courseClient = courseClient;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<CoursePageDto> getCoursesByUser(
            @PageableDefault(sort = "courseId", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable(value = "userId") UUID userId,
            @RequestHeader("Authorization") String token
    ) throws AccessDeniedException {
        UserDetailsImpl authenticatedUser = SecurityUtils.getAuthenticatedUser().orElseThrow(
                () -> new AccessDeniedException("User not authenticated")
        );
        if (authenticatedUser.getUserId().equals(userId) || SecurityUtils.isAdmin()) {
            return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId, pageable, token));
        } else {
            throw new AccessDeniedException("You do not have permission to access these courses.");
        }

    }

}
