package com.jaramgroupware.mms.utils.validation.page;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.Pageable;


import java.util.Arrays;
import java.util.List;

public class PageableSortKeyValidator implements ConstraintValidator<PageableSortKeyCheck, Pageable> {

    private List<String> sortKeys;
    @Override
    public void initialize(PageableSortKeyCheck constraintAnnotation) {
        sortKeys = Arrays.asList(constraintAnnotation.sortKeys());
    }
    @Override
    public boolean isValid(Pageable value, ConstraintValidatorContext context) {

        return value.getSort().stream().allMatch(sort -> sortKeys.contains(sort.getProperty()));
    }
}
