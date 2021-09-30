package org.matrix.zero.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.matrix.zero.entity.TokenMatrix;

@Repository
public interface TokenMatrixRepository extends MongoRepository<TokenMatrix, String> {

    TokenMatrix findByUserId(String userId);
}
