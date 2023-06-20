package com.skolarli.lmsservice.models.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TrueFalseValueValidator implements ConstraintValidator<ValidTrueFalseValue, Integer> {

    @Override
    public void initialize(ValidTrueFalseValue constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == 1 || value == 2;
    }
}

