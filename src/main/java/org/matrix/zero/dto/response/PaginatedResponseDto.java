package org.matrix.zero.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PaginatedResponseDto<T> {

    private long total;

    private int totalPages;

    private int page;

    private int perPage;

    private List<T> registers;
}
