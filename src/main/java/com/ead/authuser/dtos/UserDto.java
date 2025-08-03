package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(

        @NotBlank(groups = UserView.RegistrationPost.class, message = "Username cannot be blank")
        @Size(groups = UserView.RegistrationPost.class, min = 3, max = 50, message = "Username must be entre 3 e 50 " +
                "caracteres")
        @JsonView({UserView.RegistrationPost.class})
        String username,

        @NotBlank(groups = UserView.RegistrationPost.class, message = "Email cannot be blank")
        @Email(groups = UserView.RegistrationPost.class, message = "Email must be a valid email address")
        @Size(groups = UserView.RegistrationPost.class, min = 5, max = 150, message = "Email must be between 5 and " +
                "150 characters")
        @JsonView({UserView.RegistrationPost.class})
        String email,

        @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class}, message = "Password cannot " +
                "be blank")
        @Size(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class}, min = 6, max = 20, message =
                "Password must be between 6 and 20 characters")
        @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
        String password,

        @NotBlank(groups = {UserView.PasswordPut.class}, message = "Old password cannot be blank")
        @Size(groups = {UserView.PasswordPut.class}, min = 6, max = 20, message = "Old password must be between 6 and" +
                " 20 characters")
        @JsonView({UserView.PasswordPut.class})
        String oldPassword,

        @NotBlank(groups = {UserView.RegistrationPost.class, UserView.UserPut.class}, message = "Full name cannot be blank")
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String fullName,

        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String phoneNumber,

        @NotBlank(groups = {UserView.ImagePut.class}, message = "Image cannot be blank")
        @JsonView({UserView.ImagePut.class})
        String imageUrl

) {
    public interface UserView {
        interface RegistrationPost {
        }

        interface UserPut {
        }

        interface PasswordPut {
        }

        interface ImagePut {
        }
    }
}
