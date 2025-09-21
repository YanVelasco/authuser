package com.ead.authuser.service.impl;

import com.ead.authuser.controllers.UserController;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.dtos.UserPageDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.service.UserService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserModel getUserById(UUID userId) {
        logger.debug("Get user by id: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
    }

    @Override
    public void deleteUserById(UserModel userModel) {
        userRepository.delete(userModel);
    }

    @Override
    public UserModel registerUser(UserDto userDto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.USER);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public UserModel updateUser(UserModel userModel, UserDto userDto) {
        logger.debug("UserDto received {}", userDto);
        userModel.setFullName(userDto.fullName());
        userModel.setPhoneNumber(userDto.phoneNumber());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public UserModel updatePassword(UserModel userModel, UserDto userDto) {
        logger.debug("New password {} received ", userDto.password());
        userModel.setPassword(userDto.password());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public UserModel updateUserImage(UserModel userModel, UserDto userDto) {
        logger.debug("New image {} received ", userDto.imageUrl());
        userModel.setImageUrl(userDto.imageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public UserPageDto findAll(Pageable pageable, String fullName, UserStatus userStatus, UserType userType,
                               String username, String email, UUID courseId) {

        Specification<UserModel> spec = (root, query, cb) -> cb.conjunction();

        if (fullName != null && !fullName.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("fullName")),
                    "%" + fullName.toLowerCase() + "%"));
        }
        if (userStatus != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("userStatus"), userStatus));
        }
        if (userType != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("userType"), userType));
        }
        if (username != null && !username.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("username")),
                    "%" + username.toLowerCase() + "%"));
        }
        if (email != null && !email.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }

        if (courseId != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Object, Object> userCourseJoin = root.join("userCourses", JoinType.INNER);
                return cb.equal(userCourseJoin.get("courseId"), courseId);
            });
        }


        var pageResult = userRepository.findAll(spec, pageable);

        if (!pageResult.isEmpty()) {
            for (UserModel userModel : pageResult) {
                userModel.add(linkTo(methodOn(UserController.class).getUserById(userModel.getUserId())).withSelfRel());
            }
        }

        return UserPageDto.from(pageResult);
    }

    @Override
    public UserModel saveSubscriptionInstructor(UserModel userModel) {
        userModel.setUserType(UserType.INSTRUCTOR);
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
