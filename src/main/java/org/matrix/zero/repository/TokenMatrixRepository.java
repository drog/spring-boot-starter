package org.matrix.zero.repository;

import org.matrix.zero.entity.TokenMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenMatrixRepository extends JpaRepository<TokenMatrix, Long> {
}
