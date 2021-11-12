package com.company.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class PasswordDto {

    public static final int MIN_LENGTH = 5;
    public static final int MAX_LENGTH = 60;

    @Size(min = MIN_LENGTH, max = MAX_LENGTH, message = "{com.company.dto.passwordDto.password.sizeConstraint.message}")
    private char[] password;

    @Size(min = MIN_LENGTH, max = MAX_LENGTH, message = "{com.company.dto.passwordDto.confirmedPassword.sizeConstraint.message}")
    private char[] confirmedPassword;
}
