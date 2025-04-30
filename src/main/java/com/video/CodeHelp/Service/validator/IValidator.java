package com.video.CodeHelp.Service.validator;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.commons.lang3.StringUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;

public abstract class IValidator<T> {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator= factory.getValidator();

    public String genericValidate(T t) {
        StringBuilder errorBuilder = new StringBuilder();
        List<String> customErrors = new ArrayList<>();
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        for (ConstraintViolation<T> violation : violations) {
            customErrors.add(violation.getMessage());
        }
        validate(t, customErrors);
        customErrors.forEach(s -> {
            if (StringUtils.isEmpty(errorBuilder)) {
                errorBuilder.append(s);
            } else {
                errorBuilder.append(",").append(s);
            }
        });
        return errorBuilder.toString();
    }


    public abstract void validate(T obj,List<String> errors);
}
