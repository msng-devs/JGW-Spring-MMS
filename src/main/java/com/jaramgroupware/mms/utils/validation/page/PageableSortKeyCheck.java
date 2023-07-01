package com.jaramgroupware.mms.utils.validation.page;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PageableSortKeyValidator.class)
@Target( { ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PageableSortKeyCheck {
    String message() default "Sort 옵션이 잘못되었습니다!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] sortKeys();
}
