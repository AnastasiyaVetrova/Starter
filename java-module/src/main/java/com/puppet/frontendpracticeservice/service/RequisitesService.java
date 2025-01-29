package com.puppet.frontendpracticeservice.service;

import com.puppet.frontendpracticeservice.domain.request.RequisitesDto;

import java.util.UUID;

public interface RequisitesService {
    /**
     * Позволяет получить имя пользователя, его расчетный счет и кбк
     *
     * @param user_id id пользователя
     * @return RequisitesDto
     */
    RequisitesDto findRequisites(UUID user_id);
}
