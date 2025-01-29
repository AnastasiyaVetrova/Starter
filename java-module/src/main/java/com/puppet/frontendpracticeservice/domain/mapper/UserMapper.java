package com.puppet.frontendpracticeservice.domain.mapper;

import com.puppet.frontendpracticeservice.domain.entity.User;
import com.puppet.frontendpracticeservice.domain.request.UserRequestDto;
import com.puppet.frontendpracticeservice.domain.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponseDto userToUserResponseDto(User user);

    @Mapping(target = "id", ignore = true)
    User userRequestDtoToUser(UserRequestDto user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User updateUserRequestDtoToUser(@MappingTarget User user, UserRequestDto userRequestDto);
}