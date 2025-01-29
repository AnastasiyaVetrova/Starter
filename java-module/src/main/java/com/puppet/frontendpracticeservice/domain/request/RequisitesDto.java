package com.puppet.frontendpracticeservice.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Реквизиты пользователя")
public record RequisitesDto(
        String name,
        String surname,
        String currentAccount,
        String kbk) {
}