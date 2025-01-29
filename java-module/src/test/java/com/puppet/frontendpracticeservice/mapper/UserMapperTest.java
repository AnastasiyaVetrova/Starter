package com.puppet.frontendpracticeservice.mapper;

import com.puppet.frontendpracticeservice.domain.entity.Requisites;
import com.puppet.frontendpracticeservice.domain.entity.User;
import com.puppet.frontendpracticeservice.domain.mapper.UserMapper;
import com.puppet.frontendpracticeservice.domain.request.UserRequestDto;
import com.puppet.frontendpracticeservice.domain.response.UserResponseDto;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.TreeSet;
import java.util.UUID;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void userToUserResponseDto() {
        User user = getUser();

        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(user);

        assertAll(
                "Проверка нескольких полей", () -> {
                    assertEquals(user.getName(), userResponseDto.name());
                    assertEquals(user.getBirthday(), userResponseDto.birthday());
                    assertEquals(user.getInn(), userResponseDto.inn());
                });
    }

    @Test
    void userRequestDtoToUser() {
        UserRequestDto userRequestDto = getUserDto();

        User user = userMapper.userRequestDtoToUser(userRequestDto);

        assertAll(
                "Проверка нескольких полей", () -> {
                    assertEquals(user.getName(), userRequestDto.name());
                    assertEquals(user.getBirthday(), userRequestDto.birthday());
                    assertEquals(user.getInn(), userRequestDto.inn());
                });
    }

    @Test
    void updateUserRequestDtoToUser() {
        UserRequestDto userRequestDto = new UserRequestDto(null, "Васильев", null,
                null, null, null, null, null, null);
        User user = getUser();

        User userUpdated = userMapper.updateUserRequestDtoToUser(getUser(), userRequestDto);

        assertAll(
                "Проверка нескольких полей",
                () -> assertEquals(userRequestDto.surname(), userUpdated.getSurname()),
                () -> assertEquals(user.getId(), userUpdated.getId()),
                () -> assertEquals(user.getName(), userUpdated.getName()),
                () -> assertEquals(user.getBirthday(), userUpdated.getBirthday()),
                () -> assertEquals(user.getInn(), userUpdated.getInn())
        );
    }

    private UserRequestDto getUserDto() {
        return new UserRequestDto(
                "Иван",
                "Иванов",
                LocalDate.of(2000, 10, 25),
                "111111111111",
                "11111111111",
                "1111111111",
                "Ваня11",
                "ваня1111",
                new TreeSet<>());
    }

    private User getUser() {
        return new User(
                UUID.fromString("fc4243ea-e8e9-4d10-ab25-e80b32544405"),
                "Иван",
                "Иванов",
                LocalDate.of(2000, 10, 25),
                "111111111111",
                "11111111111",
                "1111111111",
                "Ваня11",
                "ваня1111",
                new TreeSet<>(),
                new Requisites());
    }
}