package org.matrix.zero.service;

import lombok.extern.slf4j.Slf4j;
import org.matrix.zero.entity.TokenMatrix;
import org.matrix.zero.entity.User;
import org.matrix.zero.repository.TokenMatrixRepository;
import org.matrix.zero.utils.TokenUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenMatrixService {

    private final TokenMatrixRepository tokenMatrixRepository;

    public TokenMatrixService(TokenMatrixRepository tokenMatrixRepository) {
        this.tokenMatrixRepository = tokenMatrixRepository;
    }

    public TokenMatrix createByUser(User user) {
        TokenMatrix tokenMatrix = new TokenMatrix();
        tokenMatrix.setUser(user);
        tokenMatrix.setToken(TokenUtils.generateToken());
        return tokenMatrixRepository.save(tokenMatrix);
    }
}
