package org.matrix.zero.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatrixIdentityRequestDto {

    private Long userId;

    private String token;
}
