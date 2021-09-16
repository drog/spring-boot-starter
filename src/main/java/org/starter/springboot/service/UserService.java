package org.starter.springboot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.starter.springboot.dto.external.MatrixIdentityDto;
import org.starter.springboot.dto.request.UserRequest;
import org.starter.springboot.dto.response.UserDto;
import org.starter.springboot.entity.TokenMatrix;
import org.starter.springboot.entity.User;
import org.starter.springboot.exception.UserException;
import org.starter.springboot.mapper.UserMapper;
import org.starter.springboot.repository.UserRepository;
import org.starter.springboot.utils.RestTemplateService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RestTemplateService restTemplateService;

    private final TokenMatrixService tokenMatrixService;

    public UserService(UserRepository userRepository,
                       RestTemplateService restTemplateService,
                       TokenMatrixService tokenMatrixService) {
        this.userRepository = userRepository;
        this.restTemplateService = restTemplateService;
        this.tokenMatrixService = tokenMatrixService;
    }

    public UserDto createUser(UserRequest userRequest) throws UserException {
        User user = userRepository.findByEmail(userRequest.getEmail());
        if (user == null) {
            user = new User()
                    .setEmail(userRequest.getEmail())
                    .setFirstName(userRequest.getFirstName())
                    .setLastName(userRequest.getLastName())
                    .setAge(userRequest.getAge());

            user = userRepository.save(user);

            UserDto userDto = UserMapper.toUserDto(user);
            getAdditionalData(userDto, user.getId());
            return userDto;
        }
        log.warn("user duplicated");
        throw new UserException("user duplicated");
    }

    public UserDto updateUser(UserRequest userRequest) throws UserException {
        User user = userRepository.findByEmail(userRequest.getEmail());
        if (user != null) {
            user.setEmail(userRequest.getEmail())
                    .setFirstName(userRequest.getFirstName())
                    .setLastName(userRequest.getLastName());
            UserDto userDto =  UserMapper.toUserDto(userRepository.save(user));
            getAdditionalData(userDto, user.getId());
            return userDto;
        }
        log.warn("user notFound");
        throw new UserException("notFound");
    }

    public List<UserDto> findAll() {
        List<UserDto> result = new ArrayList<>();

        List<User> userList = userRepository.findAll();
        if(!CollectionUtils.isEmpty(userList)) {
            UserDto userDto;
            for (User user : userList) {
                userDto = UserMapper.toUserDto(user);
                getAdditionalData(userDto, user.getId());
                result.add(userDto);
            }
        }
        return result;
    }

    public UserDto findUserByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("user notFound");
            throw new UserException("notFound");
        }
        UserDto userDto = UserMapper.toUserDto(user);
        getAdditionalData(userDto, user.getId());
        return userDto;
    }

    public void deleteUserByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("user notFound");
            throw new UserException("notFound");
        }
        userRepository.delete(user);
    }

    private void getAdditionalData(UserDto userDto, String userId) {
        TokenMatrix tokenMatrix = tokenMatrixService.findByUserId(userId);
        userDto.setTokenMatrix(tokenMatrix.getToken());
        MatrixIdentityDto matrixIdentityDto = restTemplateService.getIdentityInMatrix(userId);
        userDto.setMatrixIdentity(matrixIdentityDto);
    }
}