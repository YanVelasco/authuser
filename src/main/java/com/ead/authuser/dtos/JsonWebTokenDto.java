package com.ead.authuser.dtos;

import jakarta.validation.constraints.NotBlank;

public record JsonWebTokenDto(

        @NotBlank(message = "Token cannot be null") String token
        ,
        String type

) {

    public JsonWebTokenDto(@NotBlank String token) {
        this(token, "Bearer");
    }

}
