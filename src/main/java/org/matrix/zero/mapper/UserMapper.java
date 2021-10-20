package org.matrix.zero.mapper;

import org.matrix.zero.dto.response.UserDto;
import org.matrix.zero.entity.User;
import org.modelmapper.ModelMapper;

public class UserMapper {

    private static ModelMapper modelMapper = new ModelMapper();

    public static UserDto toUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
