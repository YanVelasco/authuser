package com.ead.authuser.service.impl;

import com.ead.authuser.repository.UserCourseRepository;
import com.ead.authuser.service.UserCourseService;

public class UserCourseRepositoryImpl implements UserCourseService {

    final UserCourseRepository repository;

    public UserCourseRepositoryImpl(UserCourseRepository repository) {
        this.repository = repository;
    }

}
