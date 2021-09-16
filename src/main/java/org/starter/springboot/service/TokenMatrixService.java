package org.starter.springboot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.starter.springboot.entity.TokenMatrix;
import org.starter.springboot.repository.TokenMatrixRepository;
import org.starter.springboot.utils.TokenUtils;

@Slf4j
@Service
public class TokenMatrixService {

    private final TokenMatrixRepository tokenMatrixRepository;

    public TokenMatrixService(TokenMatrixRepository tokenMatrixRepository) {
        this.tokenMatrixRepository = tokenMatrixRepository;
    }

    @Cacheable(value = {"tokenMatrix"}, key = "{#userId}", unless="#result == null")
    public TokenMatrix findByUserId(String userId) {
        return tokenMatrixRepository.findByUserId(userId);
    }

    @CachePut(value = {"tokenMatrix"}, key = "{#userId}", unless="#result == null")
    public TokenMatrix createByUserId(String userId) {
        TokenMatrix tokenMatrix = new TokenMatrix();
        tokenMatrix.setUserId(userId);
        tokenMatrix.setToken(TokenUtils.generateToken());
        return tokenMatrixRepository.save(tokenMatrix);
    }
}
