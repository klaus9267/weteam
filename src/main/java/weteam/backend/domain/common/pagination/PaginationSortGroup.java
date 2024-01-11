package weteam.backend.domain.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public enum PaginationSortGroup {
    PROJECT(List.of(PaginationSortType.DONE)),
    EMPTY(List.of(PaginationSortType.EMPTY));

    private final List<PaginationSortType> sortTypes;
}
