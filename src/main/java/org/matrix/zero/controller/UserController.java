package org.matrix.zero.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.matrix.zero.dto.request.UserRequest;
import org.matrix.zero.dto.response.PaginatedResponseDto;
import org.matrix.zero.dto.response.UserDto;
import org.matrix.zero.exception.UserException;
import org.matrix.zero.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    @ApiImplicitParam(name = "x-country", value = "x-country", required = true, paramType = "header", dataTypeClass = String.class, example = "CL")
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
    @ApiImplicitParam(name = "x-country", value = "x-country", required = true, paramType = "header", dataTypeClass = String.class, example = "CL")
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
    @ApiImplicitParam(name = "x-country", value = "x-country", required = true, paramType = "header", dataTypeClass = String.class, example = "CL")
    @ApiOperation(value = "find all users")
    public ResponseEntity<PaginatedResponseDto<UserDto>> findAll(@RequestParam("page") int page,
                                                                 @RequestParam("size") int size) {
        PaginatedResponseDto<UserDto> users = userService.findAll(PageRequest.of(page, size));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{email}")
    @ApiImplicitParam(name = "x-country", value = "x-country", required = true, paramType = "header", dataTypeClass = String.class, example = "CL")
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
    @ApiImplicitParam(name = "x-country", value = "x-country", required = true, paramType = "header", dataTypeClass = String.class, example = "CL")
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