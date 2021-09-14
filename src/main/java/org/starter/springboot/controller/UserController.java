package org.starter.springboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.starter.springboot.dto.entity.UserDto;
import org.starter.springboot.entity.User;
import org.starter.springboot.mapper.UserMapper;
import org.starter.springboot.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto userDto) {
        User user = userService.createUser(userDto);
        return new ResponseEntity<>(UserMapper.toUserDto(user), HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> findByEmail(@PathVariable String email) {
        User user = userService.findUserByEmail(email);
        return new ResponseEntity<>(UserMapper.toUserDto(user), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> findAll() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(UserMapper.toUserDtoList(users), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto userDto) {
        User user = userService.updateUser(userDto);
        return new ResponseEntity<>(UserMapper.toUserDto(user), HttpStatus.OK);
    }


}