package org.starter.springboot.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.starter.springboot.dto.response.UserDto;
import org.starter.springboot.dto.request.UserRequest;
import org.starter.springboot.exception.UserException;
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
    @ApiOperation(value = "Create an user", notes = "the email is unique")
    @ApiResponses(value = { @ApiResponse(code = 409, message = "Conflict", response = String.class)})
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserRequest userRequest) {
        try {
            UserDto userDto = userService.createUser(userRequest);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @GetMapping("/{email}")
    @ApiOperation(value = "find an user by email")
    public ResponseEntity<UserDto> findByEmail(@PathVariable String email) {
        try {
            UserDto userDto = userService.findUserByEmail(email);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping()
    @ApiOperation(value = "find all users")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{email}")
    @ApiOperation(value = "delete a user")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        try {
            userService.deleteUserByEmail(email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping
    @ApiOperation(value = "update an user")
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserRequest userRequest) {
        try {
            UserDto userDto = userService.updateUser(userRequest);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}