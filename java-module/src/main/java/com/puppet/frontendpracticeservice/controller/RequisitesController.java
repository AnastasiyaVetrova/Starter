package com.puppet.frontendpracticeservice.controller;

import com.puppet.frontendpracticeservice.domain.request.RequisitesDto;
import com.puppet.frontendpracticeservice.domain.response.SimpleMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Контроллер для работы с реквизитами пользователя")
public interface RequisitesController {

    @Operation(summary = "Позволяет получить имя пользователя, его расчетный счет и кбк",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RequisitesDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT FOUND",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SimpleMessage.class)))
            })
    ResponseEntity<RequisitesDto> getRequisites(UUID user_id);
}