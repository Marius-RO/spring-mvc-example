package com.company.validations;

import com.company.dto.PasswordDto;
import com.company.util.GeneralUtilities;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, PasswordDto> {

    private int minLength;
    private int maxLength;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        minLength = constraintAnnotation.minLength();
        maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(PasswordDto value, ConstraintValidatorContext context) {
        char[] trimmedPassword = GeneralUtilities.trimCharArray(value.getPassword());
        char[] trimmedConfirmedPassword = GeneralUtilities.trimCharArray(value.getConfirmedPassword());
        return minLength <= trimmedPassword.length && trimmedPassword.length <= maxLength &&
                minLength <= trimmedConfirmedPassword.length && trimmedConfirmedPassword.length <= maxLength &&
                Arrays.equals(trimmedPassword, trimmedConfirmedPassword);
    }
}
