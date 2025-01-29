package com.puppet.frontendpracticeservice.service.impl;

import com.puppet.frontendpracticeservice.domain.entity.User;
import com.puppet.frontendpracticeservice.domain.mapper.UserMapper;
import com.puppet.frontendpracticeservice.domain.request.UserRequestDto;
import com.puppet.frontendpracticeservice.domain.response.UserResponseDto;
import com.puppet.frontendpracticeservice.exception.UserNotFoundException;
import com.puppet.frontendpracticeservice.repository.UserRepository;
import com.puppet.frontendpracticeservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь с логином '%s' не найден", login)));
    }

    @Override
    public UserResponseDto findById(UUID id) {
        return userMapper.userToUserResponseDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с не найден")));
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::userToUserResponseDto).toList();
    }

    @Override
    public void save(UserRequestDto user) {
        userRepository.save(userMapper.userRequestDtoToUser(user));
    }

    @Transactional
    @Override
    public void update(UUID id, UserRequestDto userDTO) {
        User userToUpdate = userRepository.save(userMapper.updateUserRequestDtoToUser(
                userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с не найден")), userDTO));
        userRepository.saveAndFlush(userToUpdate);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}