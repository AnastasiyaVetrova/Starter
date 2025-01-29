package com.puppet.frontendpracticeservice.domain.request;

import com.puppet.frontendpracticeservice.security.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Данные нового пользователя")
public record UserRequestDto (
        @Nullable String name,
        @Nullable String surname,
        @Nullable LocalDate birthday,
        @Nullable String inn,
        @Nullable String snils,
        @Nullable String passport,
        @Nullable String login,
        @Nullable String password,
        @Nullable Set<Role> roles){
}