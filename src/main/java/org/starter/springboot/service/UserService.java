package org.starter.springboot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.starter.springboot.dto.entity.UserDto;
import org.starter.springboot.entity.User;
import org.starter.springboot.exception.UserException;
import org.starter.springboot.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            user = new User()
                    .setEmail(userDto.getEmail())
                    .setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName())
                    .setAge(userDto.getAge());

            return userRepository.save(user);
        }
        log.warn("user duplicated");
        throw new UserException("user duplicated");
    }

    public User updateUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user != null) {
            user.setEmail(userDto.getEmail())
                    .setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName());
            return userRepository.save(user);
        }
        log.warn("user notFound");
        throw new UserException("notFound");
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("user notFound");
            throw new UserException("notFound");
        }
        return user;
    }

    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("user notFound");
            throw new UserException("notFound");
        }
        userRepository.delete(user);
    }
}