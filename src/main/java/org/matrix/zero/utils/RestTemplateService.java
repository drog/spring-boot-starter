package org.matrix.zero.utils;

import org.matrix.zero.dto.external.MatrixIdentityDto;
import org.matrix.zero.dto.external.MatrixIdentityRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService {

    @Value("${server.matrix.url}")
    private String matrixUrl;

    private final RestTemplate restTemplate;

    public RestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MatrixIdentityDto getIdentityInMatrix(Long userId, String token) {
        String url = new StringBuilder()
                .append(matrixUrl)
                .append("identitymatrix/")
                .append(userId)
                .append("/token/")
                .append(token)
                .toString();

        ResponseEntity<MatrixIdentityDto> response = restTemplate.getForEntity(url, MatrixIdentityDto.class);
        if( response.getStatusCode().is2xxSuccessful() ) {
            return response.getBody();
        }
        return null;
    }


    public MatrixIdentityDto createIdentityInMatrix(Long userId, String token) {
        HttpEntity<MatrixIdentityRequestDto> request = new HttpEntity<>(new MatrixIdentityRequestDto(userId, token));
        ResponseEntity<MatrixIdentityDto> response = restTemplate.postForEntity(matrixUrl + "identitymatrix/", request, MatrixIdentityDto.class);
        if( response.getStatusCode().is2xxSuccessful() ) {
            return response.getBody();
        }
        return null;
    }
}