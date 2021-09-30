package org.matrix.zero.service;

import lombok.extern.slf4j.Slf4j;
import org.matrix.zero.repository.TokenMatrixRepository;
import org.matrix.zero.utils.TokenUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.matrix.zero.entity.TokenMatrix;

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
