package weteam.backend.domain.common.pagination.param;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import weteam.backend.domain.common.pagination.PaginationSortGroup;
import weteam.backend.domain.common.pagination.PaginationSortType;
import weteam.backend.domain.common.pagination.PaginationValidation;

@Getter
public class MeetingPaginationParam extends AbstractPaginationParam {
    @Parameter(example = "0", required = true)
    private final Integer page;

    @Parameter(example = "10", required = true)
    private final Integer size;

    @Parameter(name = "direction", description = "default desc")
    private final Sort.Direction direction;

    @Parameter(name = "field", description = "default started_at | started_at만 사용 가능")
    @PaginationValidation(sortGroup = PaginationSortGroup.MEETING)
    private final PaginationSortType field;

    public MeetingPaginationParam(final Integer page, final Integer size, final PaginationSortType field, final Sort.Direction direction) {
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 10);
        this.field = field == null ? PaginationSortType.STARTED_AT : field;
        this.direction = direction == null ? Sort.Direction.DESC : direction;
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, field.toSort(direction));
    }
}
