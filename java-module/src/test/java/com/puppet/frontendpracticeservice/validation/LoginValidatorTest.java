package com.puppet.frontendpracticeservice.validation;

import com.puppet.frontendpracticeservice.validation.annotation.ValidLogin;
import jakarta.validation.ConstraintValidatorContext;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginValidatorTest {
    private LoginValidator validator;
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
    private final ValidLogin annotation = mock(ValidLogin.class);

    @BeforeEach
    void setUp() {
        validator = new LoginValidator();

        when(annotation.min()).thenReturn(5);
        when(annotation.max()).thenReturn(10);
        when(annotation.message()).thenReturn("Test");

        validator.initialize(annotation);
    }

    @ParameterizedTest
    @ValueSource(strings = {"User123", "Valid@Log", "Test Login"})
    void testValidLogin(String login) {
        assertTrue(validator.isValid(login, context));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "@1234567890", "Q"})
    void testInvalidLogin(String login) {
        assertFalse(validator.isValid(login, context));
    }

    @ParameterizedTest
    @ValueSource(strings = {"@1245", "qWerty", "1qwerty"})
    void testInvalidNameLogin(String login) {
        ConstraintViolationBuilder violationBuilder = mock(ConstraintViolationBuilder.class);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addConstraintViolation()).thenReturn(context);

        assertFalse(validator.isValid(login, context));

        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(anyString());
        verify(violationBuilder).addConstraintViolation();
    }
}