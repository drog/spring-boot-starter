package org.matrix.zero.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matrix.zero.dto.external.MatrixIdentityDto;
import org.matrix.zero.dto.request.UserRequest;
import org.matrix.zero.dto.response.PaginatedResponseDto;
import org.matrix.zero.dto.response.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int randomServerPort;

    @Value("${server.matrix.url}")
    private String matrixUrl;

    @Autowired
    private TestRestTemplate restTemplateTest;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void init(){
        mockServer = MockRestServiceServer.createServer(this.restTemplate);

        MatrixIdentityDto matrixIdentityDto = mockMatrixIdentityDto();

        mockServer.expect(ExpectedCount.manyTimes(),
                        requestTo(matrixUrl + "identitymatrix/"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(asJsonString(matrixIdentityDto))
                );

        mockServer.expect(ExpectedCount.manyTimes(),
                        requestTo(MatchesPattern.matchesPattern(matrixUrl + "identitymatrix/.*")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(asJsonString(matrixIdentityDto))
                );

        headers.set("x-country", "CL");
    }

    @Test
    public void testCreateUser() {
        ResponseEntity<UserDto> response = createUser("test1@email.org", "John", "Smith", 29);
        Assertions.assertTrue(HttpStatus.OK.equals(response.getStatusCode()));
    }

    @Test
    public void testCreateDuplicated() {
        ResponseEntity<UserDto> response = createUser("test9@email.org", "John", "Smith", 29);
        ResponseEntity<UserDto> response2 = createUser("test9@email.org", "John", "Smith", 29);
        Assertions.assertTrue(HttpStatus.OK.equals(response.getStatusCode()));
        Assertions.assertTrue(HttpStatus.CONFLICT.equals(response2.getStatusCode()));
    }

    @Test
    public void testCreateUserWrongEmail() {
        ResponseEntity<UserDto> response = createUser("wrongEmail", "John", "Smith", 29);
        Assertions.assertTrue(HttpStatus.BAD_REQUEST.equals(response.getStatusCode()));
    }

    @Test
    public void testCreateUserEmptyEmail() {
        ResponseEntity<UserDto> response = createUser(null, "John", "Smith", 29);
        Assertions.assertTrue(HttpStatus.BAD_REQUEST.equals(response.getStatusCode()));
    }

    @Test
    public void testFindOneUserByEmail() {
        createUser("test2@email.org", "John", "Smith", 29);
        HttpEntity<UserRequest> request = new HttpEntity<>( headers);
        ResponseEntity<UserDto> response = this.restTemplateTest.exchange(createURLWithPort("/users/test2@email.org"),HttpMethod.GET, request, UserDto.class);
        Assertions.assertTrue(HttpStatus.OK.equals(response.getStatusCode()));
    }

    @Test
    public void testFindOneUserNotFoundByEmail() {
        HttpEntity<UserRequest> request = new HttpEntity<>( headers);
        ResponseEntity<UserDto> response = this.restTemplateTest.exchange(createURLWithPort("/users/emailNotFound@email.org"),HttpMethod.GET, request, UserDto.class);
        Assertions.assertTrue(HttpStatus.NO_CONTENT.equals(response.getStatusCode()));
    }

    @Test
    public void testFindAllUsers() {
        createUser("test3@email.org", "John", "Smith", 29);
        HttpEntity<UserRequest> request = new HttpEntity<>( headers);
        ResponseEntity<PaginatedResponseDto> response = this.restTemplateTest.exchange(createURLWithPort("/users?page=0&size=5"),HttpMethod.GET, request, PaginatedResponseDto.class);
        System.out.println(response);
        Assertions.assertTrue(HttpStatus.OK.equals(response.getStatusCode()));
    }

    @Test
    public void testUpdateUser() {
        createUser("test5@email.org", "John", "Smith", 29);
        UserRequest userRequest = new UserRequest("test5@email.org", "John", "Smith", 20);
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        ResponseEntity<UserDto> response = this.restTemplateTest.exchange(createURLWithPort("/users"), HttpMethod.PUT, request, UserDto.class);
        Assertions.assertTrue(HttpStatus.OK.equals(response.getStatusCode()));
    }

    @Test
    public void testUpdateUserNotFound() {
        UserRequest userRequest = new UserRequest("emailNotFound@email.org", "John", "Smith", 20);
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        ResponseEntity<UserDto> response = this.restTemplateTest.exchange(createURLWithPort("/users"), HttpMethod.PUT, request, UserDto.class);
        Assertions.assertTrue(HttpStatus.NO_CONTENT.equals(response.getStatusCode()));
    }

    @Test
    public void testDeleteUser() {
        createUser("test4@email.org", "John", "Smith", 29);
        HttpEntity<UserRequest> request = new HttpEntity<>(headers);
        this.restTemplateTest.exchange(createURLWithPort("/users/test4@email.org"), HttpMethod.DELETE, request, Void.class);
    }

    @Test
    public void testDeleteUserNotFound() {
        HttpEntity<UserRequest> request = new HttpEntity<>(headers);
        this.restTemplateTest.exchange(createURLWithPort("/users/emailNotFound@email.org"), HttpMethod.DELETE, request, Void.class);
    }

    private ResponseEntity<UserDto> createUser(String email, String firstName, String lastName, Integer age) {
        UserRequest userRequest = new UserRequest(email, firstName, lastName, age);
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        return this.restTemplateTest.postForEntity(createURLWithPort("/users"), request, UserDto.class);
    }

    private MatrixIdentityDto mockMatrixIdentityDto() {
        MatrixIdentityDto matrixIdentityDto = new MatrixIdentityDto();
        matrixIdentityDto.setNick("nick");
        matrixIdentityDto.setAge(29);
        matrixIdentityDto.setFirstName("firstName");
        matrixIdentityDto.setLastName("lastName");
        matrixIdentityDto.setPhone("123456789");

        return matrixIdentityDto;
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + randomServerPort + uri;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
