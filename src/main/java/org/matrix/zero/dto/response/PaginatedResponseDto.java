package org.matrix.zero.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponseDto<T> {

    private long total;

    private int totalPages;

    private int page;

    private int perPage;

    private List<T> data;
}
