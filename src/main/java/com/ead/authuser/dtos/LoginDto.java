package com.ead.authuser.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(

        @NotBlank(message = "Username cannot be null") String username,

        @NotBlank(message = "Password cannot be null") String password

) {
}
