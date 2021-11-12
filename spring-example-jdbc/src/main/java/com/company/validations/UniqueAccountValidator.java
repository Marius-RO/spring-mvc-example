package com.company.validations;

import com.company.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueAccountValidator implements ConstraintValidator<UniqueAccount, String> {

    private final AccountService accountService;

    @Autowired
    public UniqueAccountValidator(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // every new account must have a new email
        return !accountService.userExists(value);
    }
}
