package com.company.validations;

import com.company.dto.PasswordDto;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPasswordValidator.class)
public @interface ValidPassword {

    String message() default "{com.company.validations.validPasswordConstraint.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    int minLength() default PasswordDto.MIN_LENGTH;
    int maxLength() default PasswordDto.MAX_LENGTH;
}
