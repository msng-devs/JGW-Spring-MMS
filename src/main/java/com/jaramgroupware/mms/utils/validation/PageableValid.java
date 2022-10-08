package com.jaramgroupware.mms.utils.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PageableValidator.class)
@Target( { ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface  PageableValid {
    String message() default "Sort 옵션이 잘못되었습니다!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] sortKeys();
}
