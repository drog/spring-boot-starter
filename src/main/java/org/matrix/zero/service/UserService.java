package org.matrix.zero.service;

import lombok.extern.slf4j.Slf4j;
import org.matrix.zero.dto.external.MatrixIdentityDto;
import org.matrix.zero.dto.request.UserRequest;
import org.matrix.zero.dto.response.UserDto;
import org.matrix.zero.entity.TokenMatrix;
import org.matrix.zero.entity.User;
import org.matrix.zero.exception.UserException;
import org.matrix.zero.mapper.UserMapper;
import org.matrix.zero.repository.UserRepository;
import org.matrix.zero.utils.RestTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
            createAdditionalData(userDto, user);
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

    private void getAdditionalData(UserDto userDto, Long userId) {
        TokenMatrix tokenMatrix = tokenMatrixService.findByUserId(userId);
        if( tokenMatrix != null ) {
            userDto.setTokenMatrix(tokenMatrix.getToken());
        }
        MatrixIdentityDto matrixIdentityDto = restTemplateService.getIdentityInMatrix(userId);
        userDto.setMatrixIdentity(matrixIdentityDto);
    }

    private void createAdditionalData(UserDto userDto, User user) {
        TokenMatrix tokenMatrix = tokenMatrixService.createByUser(user);
        if( tokenMatrix != null ) {
            userDto.setTokenMatrix(tokenMatrix.getToken());
        }
        MatrixIdentityDto matrixIdentityDto = restTemplateService.createIdentityInMatrix(user.getId());
        userDto.setMatrixIdentity(matrixIdentityDto);
    }
}