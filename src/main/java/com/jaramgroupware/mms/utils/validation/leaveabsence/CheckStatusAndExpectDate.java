package com.jaramgroupware.mms.utils.validation.leaveabsence;

import com.jaramgroupware.mms.utils.validation.page.PageableSortKeyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PageableSortKeyValidator.class)
@Target( { ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface  CheckStatusAndExpectDate {

}
