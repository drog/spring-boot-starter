package org.matrix.zero.controller;

import org.junit.jupiter.api.Test;
import org.matrix.zero.dto.request.UserRequest;
import org.matrix.zero.dto.response.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

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


    private String createURLWithPort(String uri) {
        return "http://localhost:" + randomServerPort + uri;
    }
}
