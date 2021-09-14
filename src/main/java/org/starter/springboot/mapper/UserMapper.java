package org.starter.springboot.mapper;

import org.modelmapper.ModelMapper;
import org.starter.springboot.dto.entity.UserDto;
import org.starter.springboot.entity.User;

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
