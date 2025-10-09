package com.ead.authuser.dtos;

import lombok.Builder;

import java.util.UUID;

public record UserEventDto(
        UUID userId,
        String username,
        String email,
        String fullName,
        String userStatus,
        String userType,
        String phoneNumber,
        String imageUrl,
        String actionType
) {
}