package com.company.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidEmployeeAdditionValidator.class)
public @interface ValidEmployeeAddition  {
    String message() default "{com.company.validations.validEmployeeAdditionConstraint.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
