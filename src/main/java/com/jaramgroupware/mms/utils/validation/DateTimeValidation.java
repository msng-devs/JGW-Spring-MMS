package com.jaramgroupware.mms.utils.validation;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DateTimeValidation implements
        ConstraintValidator<DateTimeCheck, Object> {

    private final DateTimeCompare dateTimeCompare;
    private String startDateTime;
    private String endDateTime;

    @Override
    public void initialize(DateTimeCheck constraintAnnotation) {
        this.startDateTime = constraintAnnotation.startDateTime();
        this.endDateTime = constraintAnnotation.endDateTime();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        Object startDateTimeValue = new BeanWrapperImpl(object).getPropertyValue(startDateTime);
        Object endDateTimeValue = new BeanWrapperImpl(object).getPropertyValue(endDateTime);
        return dateTimeCompare.compare((LocalDateTime) startDateTimeValue,(LocalDateTime) endDateTimeValue);
    }


}
