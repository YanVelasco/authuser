package com.ead.authuser.service;

import com.ead.authuser.models.UserModel;
import io.micrometer.observation.ObservationFilter;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserModel> getAllUsers();

    Object getUserById(UUID userId);

}
