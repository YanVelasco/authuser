package com.ead.authuser.validations;

import com.ead.authuser.validations.impl.PasswordConstraintImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {

    String message() default "Password must be between 6 and 20 characters, contain at least one uppercase letter, one lowercase letter, one digit, one special character, and must not contain spaces.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
