package org.starter.springboot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.starter.springboot.dto.external.MatrixIdentityDto;

@Service
public class RestTemplateService {

    @Value("${server.matrix.url}")
    private String matrixUrl;

    private RestTemplate restTemplate = new RestTemplate();

    public MatrixIdentityDto getIdentityInMatrix(String userId) {
        ResponseEntity<MatrixIdentityDto> response = restTemplate.getForEntity(matrixUrl + "identitymatrix/" + userId, MatrixIdentityDto.class);
        if( response.getStatusCode().is2xxSuccessful() ) {
            return response.getBody();
        }
        return null;
    }

    public MatrixIdentityDto createIdentityInMatrix(String id) {
        HttpEntity<String> request = new HttpEntity<>(id);
        ResponseEntity<MatrixIdentityDto> response = restTemplate.postForEntity(matrixUrl + "identitymatrix/", request, MatrixIdentityDto.class);
        if( response.getStatusCode().is2xxSuccessful() ) {
            return response.getBody();
        }
        return null;
    }
}
