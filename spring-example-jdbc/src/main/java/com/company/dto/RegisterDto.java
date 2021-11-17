package com.company.dto;

import com.company.validations.UniqueAccount;
import com.company.validations.ValidEmployeeAddition;
import com.company.validations.ValidPassword;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class RegisterDto {

    @Size(min = 5, max = 50, message = "{com.company.dto.registerDto.email.sizeConstraint.message}")
    @Email(regexp = "^[a-zA-Z0-9-_.]+@(yahoo|gmail).com$", message = "{com.company.dto.registerDto.email.emailConstraint.message}")
    @UniqueAccount
    private String email;

    @ValidPassword
    private PasswordDto passwordDto;

    @AssertTrue(message = "{com.company.dto.registerDto.termsCheck.assertTrueConstraint.message}")
    private boolean termsCheck;

    @ValidEmployeeAddition
    private Boolean isEmployeeCheck = Boolean.FALSE;
}
