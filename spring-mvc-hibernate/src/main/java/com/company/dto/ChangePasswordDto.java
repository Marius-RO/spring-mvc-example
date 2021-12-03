package com.company.dto;

import com.company.validations.ValidPassword;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangePasswordDto {
    private String oldPassword;

    @ValidPassword(message = "{com.company.dto.changePasswordDto.passwordDto.validPasswordConstraint.message}")
    private PasswordDto passwordDto;
}
