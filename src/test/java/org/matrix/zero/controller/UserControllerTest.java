package org.matrix.zero.controller;

import org.junit.jupiter.api.Test;
import org.matrix.zero.dto.request.UserRequest;
import org.matrix.zero.dto.response.PaginatedResponseDto;
import org.matrix.zero.dto.response.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    @Test
    public void testCreateUser() {
        UserRequest userRequest = new UserRequest("test@email.org", "John", "Smith", 20);
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest);
        ResponseEntity<UserDto> response = this.restTemplate.postForEntity(createURLWithPort("/users"), request, UserDto.class);
    }

    @Test
    public void testFindOneUserByEmail() {
        ResponseEntity<UserDto> response = this.restTemplate.getForEntity(createURLWithPort("/users/email@test.org"), UserDto.class);
    }

    @Test
    public void testFindAllUsers() {
        ResponseEntity<PaginatedResponseDto> response = this.restTemplate.getForEntity(createURLWithPort("/users?page=0&size=5"), PaginatedResponseDto.class);
    }

    @Test
    public void testDeleteUser() {
        this.restTemplate.delete(createURLWithPort("/users/test@email.org"));
    }

    @Test
    public void testUpdateUser() {
        UserRequest userRequest = new UserRequest("test@email.org", "John", "Smith", 20);
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest);
        ResponseEntity<UserDto> response = this.restTemplate.exchange(createURLWithPort("/users"), HttpMethod.PUT, request, UserDto.class);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + randomServerPort + uri;
    }
}
