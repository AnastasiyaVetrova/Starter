package com.puppet.frontendpracticeservice.service;

import com.puppet.frontendpracticeservice.domain.entity.User;
import com.puppet.frontendpracticeservice.domain.mapper.UserMapper;
import com.puppet.frontendpracticeservice.domain.request.UserRequestDto;
import com.puppet.frontendpracticeservice.domain.response.UserResponseDto;
import com.puppet.frontendpracticeservice.exception.UserNotFoundException;
import com.puppet.frontendpracticeservice.repository.TestContainerDataBase;
import com.puppet.frontendpracticeservice.repository.UserRepository;
import com.puppet.frontendpracticeservice.security.Role;
import com.puppet.frontendpracticeservice.service.impl.UserServiceImpl;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

@Import({UserServiceImpl.class})
class UserServiceImplTest extends TestContainerDataBase {
    private static final String testLogin = "Марфа3";
    private static final String invalidTestLogin = "Наташа";
    private static final UUID testId = UUID.fromString("7220e843-6558-4750-9c3e-b4444324ca3e");
    private static final UUID invalidTestId = UUID.fromString("0000e000-0000-4750-9c3e-b4444324ca3e");

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @MockBean
    private UserMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        when(mapper.userToUserResponseDto(any(User.class)))
                .thenAnswer(cap -> userMapper.userToUserResponseDto(cap.getArgument(0)));
        when(mapper.userRequestDtoToUser(any(UserRequestDto.class)))
                .thenAnswer(cap -> userMapper.userRequestDtoToUser(cap.getArgument(0)));
    }

    @Test
    void findByLogin_andReturnValidData() {
        User user = userServiceImpl.findByLogin(testLogin);

        assertEquals(testId, user.getId());
    }

    @Test
    void findByLogin_andReturnNotFound() {
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.findByLogin(invalidTestLogin));
    }

    @Test
    void findById_andReturnValidData() {
        when(mapper.userToUserResponseDto(any(User.class)))
                .thenAnswer(cap -> userMapper.userToUserResponseDto(cap.getArgument(0)));

        UserResponseDto userResponseDto = userServiceImpl.findById(testId);

        assertEquals(testLogin, userResponseDto.login());
    }

    @Test
    void findById_andReturnNotFound() {
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.findById(invalidTestId));
    }

    @Test
    void findAll_andReturnValidData() {
        List<UserResponseDto> usersList = userServiceImpl.findAll();

        verify(mapper, times(3)).userToUserResponseDto(any(User.class));
        assertEquals(3, usersList.size());
    }

    @Test
    void save_UserDoesNotExist() {
        UserRequestDto userRequestDto = getUserDto();

        userServiceImpl.save(userRequestDto);
        User user = userServiceImpl.findByLogin(userRequestDto.login());

        verify(mapper, times(1)).userRequestDtoToUser(any(UserRequestDto.class));

        assertAll(() -> {
            assertEquals(userRequestDto.name(), user.getName());
            assertEquals(userRequestDto.surname(), user.getSurname());
            assertNotNull(user.getId());
        });
    }

    @Test
    void save_UserAlreadyExists() {
        UserRequestDto userRequestDto = new UserRequestDto(null, null, null,
                null, null, null, "Ваня11", null, null);

        assertThrows(DataIntegrityViolationException.class, () -> userServiceImpl.save(userRequestDto));
    }

    @Test
    void update_andReturnValidData() {
        UserRequestDto userRequestDto = new UserRequestDto("Марфочка", null, null,
                null, null, null, null, null, null);

        when(mapper.updateUserRequestDtoToUser(any(User.class), any(UserRequestDto.class)))
                .thenAnswer(cap -> userMapper.updateUserRequestDtoToUser(cap.getArgument(0), userRequestDto));

        userServiceImpl.update(testId, userRequestDto);
        User user = userServiceImpl.findByLogin(testLogin);

        assertAll(() -> {
            verify(mapper, times(1)).updateUserRequestDtoToUser(any(User.class), any(UserRequestDto.class));
            assertEquals(testId, user.getId());
            assertEquals(userRequestDto.name(), user.getName());
            assertNotNull(user.getSurname());
        });
    }

    @Test
    void update_andReturnInvalidLogin() {
        UserRequestDto userRequestDto = new UserRequestDto(null, null, null,
                null, null, null, "Петя22", null, null);

        when(mapper.updateUserRequestDtoToUser(any(User.class), any(UserRequestDto.class)))
                .thenAnswer(cap -> userMapper.updateUserRequestDtoToUser(cap.getArgument(0), userRequestDto));

        assertThrows(DataIntegrityViolationException.class, () -> userServiceImpl.update(testId, userRequestDto));
    }

    @Test
    void delete_andReturnValidData() {
        List<UserResponseDto> usersList = userServiceImpl.findAll();

        userServiceImpl.delete(testId);
        List<UserResponseDto> newUsersList = userServiceImpl.findAll();

        assertEquals(usersList.size(), newUsersList.size() + 1);
    }

    @Test
    void delete_andReturnInvalidId() {
        List<UserResponseDto> usersList = userServiceImpl.findAll();

        userServiceImpl.delete(invalidTestId);
        List<UserResponseDto> newUsersList = userServiceImpl.findAll();

        assertEquals(usersList.size(), newUsersList.size());
    }

    private UserRequestDto getUserDto() {
        TreeSet<Role> roles = new TreeSet<>();
        roles.add(Role.ADMIN);
        return new UserRequestDto(
                "Вася",
                "Васильев",
                LocalDate.of(2005, 10, 25),
                "111111111111",
                "11111111111",
                "1111111111",
                "Василий",
                "Василий1111",
                roles);
    }
}