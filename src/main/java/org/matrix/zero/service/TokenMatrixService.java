package org.matrix.zero.service;

import lombok.extern.slf4j.Slf4j;
import org.matrix.zero.entity.TokenMatrix;
import org.matrix.zero.entity.User;
import org.matrix.zero.repository.TokenMatrixRepository;
import org.matrix.zero.utils.TokenUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenMatrixService {

    private final TokenMatrixRepository tokenMatrixRepository;

    public TokenMatrixService(TokenMatrixRepository tokenMatrixRepository) {
        this.tokenMatrixRepository = tokenMatrixRepository;
    }

    @Cacheable(value = {"tokenMatrix"}, key = "{#userId}", unless="#result == null")
    public TokenMatrix findByUserId(Long userId) {
        return tokenMatrixRepository.findByUserId(userId);
    }

    @CachePut(value = {"tokenMatrix"}, key = "{#user.id}", unless="#result == null")
    public TokenMatrix createByUser(User user) {
        TokenMatrix tokenMatrix = new TokenMatrix();
        tokenMatrix.setUser(user);
        tokenMatrix.setToken(TokenUtils.generateToken());
        return tokenMatrixRepository.save(tokenMatrix);
    }
}
