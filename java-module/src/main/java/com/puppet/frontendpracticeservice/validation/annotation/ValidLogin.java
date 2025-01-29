package com.puppet.frontendpracticeservice.validation.annotation;

import com.puppet.frontendpracticeservice.validation.LoginValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Кастомная аннотация для проверки валидности логина
 * {@link #message()} сообщение пользователя
 * {@link #min()} минимальная длина логина
 * {@link #max()} максимальная длина логина
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginValidator.class)
public @interface ValidLogin {
    String message() default "Логин не соответствует требованиям";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min() default 0;

    int max() default Integer.MAX_VALUE;
}