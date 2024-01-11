package weteam.backend.domain.common.pagination.param;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import weteam.backend.domain.common.pagination.PaginationSortType;

public abstract class AbstractPaginationParam {
    protected Integer page = 0;
    protected Integer size = 10;
    private PaginationSortType field = PaginationSortType.EMPTY;
    protected Sort.Direction direction = Sort.Direction.DESC;

    public abstract Pageable toPageable();
}
