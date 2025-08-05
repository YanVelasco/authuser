package com.ead.authuser.service.impl;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.dtos.UserPageDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.exceptions.AlreadyExistsException;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

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
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
    }

    @Override
    public void deleteUserById(UserModel userModel) {
        userRepository.delete(userModel);
    }

    @Override
    public UserModel registerUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.username()) || userRepository.existsByEmail(userDto.email())) {
            throw new AlreadyExistsException("Username or email already exists.");
        }
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
        userModel.setFullName(userDto.fullName());
        userModel.setPhoneNumber(userDto.phoneNumber());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public UserModel updatePassword(UserModel userModel, UserDto userDto) {
        userModel.setPassword(userDto.password());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public UserModel updateUserImage(UserModel userModel, UserDto userDto) {
        userModel.setImageUrl(userDto.imageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(userModel);
    }

    @Override
    public UserPageDto findAll(Pageable pageable, String fullName, UserStatus userStatus, UserType userType, String username, String email, String courseId) {

        Specification<UserModel> spec = (root, query, cb) -> cb.conjunction();

        if (fullName != null && !fullName.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%"));
        }
        if (userStatus != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("userStatus"), userStatus));
        }
        if (userType != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("userType"), userType));
        }
        if (username != null && !username.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
        }
        if (email != null && !email.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }

        var pageResult = userRepository.findAll(spec, pageable);
        return UserPageDto.from(pageResult);
    }

}
