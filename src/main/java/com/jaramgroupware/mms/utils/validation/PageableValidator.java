package com.jaramgroupware.mms.utils.validation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class PageableValidator implements ConstraintValidator<PageableValid, Pageable> {

    private List<String> sortKeys;
    @Override
    public void initialize(PageableValid constraintAnnotation) {
        sortKeys = Arrays.asList(constraintAnnotation.sortKeys());
    }
    @Override
    public boolean isValid(Pageable value, ConstraintValidatorContext context) {

        return value.getSort().stream().allMatch(sort -> sortKeys.contains(sort.getProperty()));
    }
}
