package com.puppet.frontendpracticeservice.service;

import com.puppet.frontendpracticeservice.domain.request.UserRequestDto;
import com.puppet.frontendpracticeservice.domain.response.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    /**
     * Найти пользователя
     *
     * @param id UUID пользователя
     * @return найденного пользователя
     */
    UserResponseDto findById(UUID id);

    /**
     * Найти всех пользователей
     *
     * @return список пользователей
     */
    List<UserResponseDto> findAll();

    /**
     * Сохранить нового пользователя
     *
     * @param user данные нового пользователя
     */
    void save(UserRequestDto user);

    /**
     * Обновить существующего пользователя
     *
     * @param user обновляемые данные
     */
    void update(UUID id, UserRequestDto user);

    /**
     * Удалить пользователя
     *
     * @param id UUID пользователя
     */
    void delete(UUID id);
}