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
public class ProjectPaginationParam extends AbstractPaginationParam {
    @Parameter(example = "0", required = true)
    private final Integer page;

    @Parameter(example = "10", required = true)
    private final Integer size;

    @Parameter(example = "false")
    private final boolean done;

    @Parameter(example = "1")
    private final Long userId;

    @Parameter(name = "direction", description = "default desc")
    private final Sort.Direction direction;

    @Parameter(name = "field",description = "default done | done만 사용 가능")
    @PaginationValidation(sortGroup = PaginationSortGroup.PROJECT)
    private final PaginationSortType field;

    public ProjectPaginationParam(final Integer page, final Integer size, final boolean done, final Long userId, final PaginationSortType field, final Sort.Direction direction, final String test) {
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 10);
        this.done = done;
        this.userId = userId;
        this.field = field == null ? PaginationSortType.DONE : field;
        this.direction = direction == null ? Sort.Direction.DESC : direction;
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, field.toSort(direction));
    }
}
