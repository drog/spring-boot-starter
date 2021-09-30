package org.matrix.zero.mapper;

import org.matrix.zero.dto.response.UserDto;
import org.matrix.zero.entity.User;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    private static ModelMapper modelMapper = new ModelMapper();

    public static UserDto toUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public static List<UserDto> toUserDtoList(List<User> userList) {
        return userList
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }
}