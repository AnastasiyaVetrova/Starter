package com.puppet.frontendpracticeservice.validation;

import com.puppet.frontendpracticeservice.validation.annotation.ValidLogin;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Валидатор логина
 * Логин должен начинаться с большой буквы и иметь определенную длину
 */
public class LoginValidator implements ConstraintValidator<ValidLogin, String> {

    private int min;
    private int max;

    @Override
    public void initialize(ValidLogin constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if (login == null || login.isBlank() || login.length() < min || login.length() > max) {
            return false;
        }
        if (!Character.isUpperCase(login.charAt(0))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Логин должен начинаться с буквы в верхнем регистре")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
