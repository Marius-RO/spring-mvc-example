package com.company.validations;

import com.company.config.WebAppSecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidEmployeeAdditionValidator implements ConstraintValidator<ValidEmployeeAddition, Boolean> {

    @Override
    public boolean isValid(Boolean addAnEmployeeAccount, ConstraintValidatorContext context) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return false;
        }

        // only admin could add an employee
        boolean isAdmin = false;
        for(GrantedAuthority role : authentication.getAuthorities()){
            if(WebAppSecurityConfig.Roles.ROLE_ADMIN.equals(role.getAuthority())){
                isAdmin = true;
                break;
            }
        }

        return (isAdmin && addAnEmployeeAccount) || (!isAdmin && !addAnEmployeeAccount);
    }
}
