package com.skolarli.lmsservice.models.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TrueFalseValueValidator.class)
public @interface ValidTrueFalseValue {
    String message() default "Invalid integer value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

