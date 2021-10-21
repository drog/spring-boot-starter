package org.matrix.zero.service;

import lombok.extern.slf4j.Slf4j;
import org.matrix.zero.config.filter.TenantProvider;
import org.matrix.zero.dto.external.MatrixIdentityDto;
import org.matrix.zero.dto.request.UserRequest;
import org.matrix.zero.dto.response.PaginatedResponseDto;
import org.matrix.zero.dto.response.UserDto;
import org.matrix.zero.entity.TokenMatrix;
import org.matrix.zero.entity.User;
import org.matrix.zero.exception.UserException;
import org.matrix.zero.mapper.UserMapper;
import org.matrix.zero.repository.UserRepository;
import org.matrix.zero.utils.RandomNumberUtils;
import org.matrix.zero.utils.RestTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RestTemplateService restTemplateService;

    private final TokenMatrixService tokenMatrixService;

    private final TenantProvider tenantProvider;

    public UserService(UserRepository userRepository,
                       RestTemplateService restTemplateService,
                       TokenMatrixService tokenMatrixService,
                       TenantProvider tenantProvider) {
        this.userRepository = userRepository;
        this.restTemplateService = restTemplateService;
        this.tokenMatrixService = tokenMatrixService;
        this.tenantProvider = tenantProvider;
    }

    public UserDto createUser(UserRequest userRequest) throws UserException {
        User user = userRepository.findByEmail(userRequest.getEmail());
        if (user == null) {
            user = new User()
                    .setEmail(userRequest.getEmail())
                    .setFirstName(userRequest.getFirstName())
                    .setLastName(userRequest.getLastName())
                    .setAge(userRequest.getAge())
                    .setCountry(tenantProvider.getTenant());

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
            getAdditionalData(userDto, user.getId(), user.getTokenMatrixSet());
            return userDto;
        }
        log.warn("user notFound");
        throw new UserException("notFound");
    }

    public PaginatedResponseDto<UserDto> findAll(PageRequest pageRequest) {
        Page<User> userList = userRepository.findAll(pageRequest);
        List<UserDto> userDtoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(userList.getContent())) {
            UserDto userDto;
            for (User user : userList) {
                userDto = UserMapper.toUserDto(user);
                getAdditionalData(userDto, user.getId(), user.getTokenMatrixSet());
                userDtoList.add(userDto);
            }
        }
        return new PaginatedResponseDto<>(userList.getTotalElements(),
                userList.getTotalPages(),
                pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                userDtoList);
    }

    public UserDto findUserByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("user notFound");
            throw new UserException("notFound");
        }
        UserDto userDto = UserMapper.toUserDto(user);
        getAdditionalData(userDto, user.getId(), user.getTokenMatrixSet());
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

    private void getAdditionalData(UserDto userDto, Long userId, Set<TokenMatrix> tokenMatrixSet) {
        List<MatrixIdentityDto> matrixIdentities = new ArrayList<>();
        if( !CollectionUtils.isEmpty(tokenMatrixSet) ) {
            for (TokenMatrix tokenMatrix : tokenMatrixSet) {
                MatrixIdentityDto matrixIdentityDto = restTemplateService.getIdentityInMatrix(userId, tokenMatrix.getToken());
                matrixIdentityDto.setTokenMatrix(tokenMatrix.getToken());
                matrixIdentities.add(matrixIdentityDto);
            }
        }
        userDto.setMatrixIdentities(matrixIdentities);
    }

    private void createAdditionalData(UserDto userDto, User user) {

        List<MatrixIdentityDto> matrixIdentities = new ArrayList<>();
        int numberIdentities = RandomNumberUtils.generateRandomNumberBetween();
        for (int i = 0; i <= numberIdentities; i++) {
            TokenMatrix tokenMatrix = tokenMatrixService.createByUser(user);
            if( tokenMatrix != null ) {
                MatrixIdentityDto matrixIdentityDto = restTemplateService.createIdentityInMatrix(user.getId(), tokenMatrix.getToken());
                matrixIdentityDto.setTokenMatrix(tokenMatrix.getToken());
                matrixIdentities.add(matrixIdentityDto);
            }
        }
        userDto.setMatrixIdentities(matrixIdentities);
    }
}