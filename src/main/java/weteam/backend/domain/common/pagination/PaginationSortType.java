package weteam.backend.domain.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@Getter
public enum PaginationSortType {
    DONE("done"),
    STARTED_AT("startedAt"),
    PROJECT_ID("projectId"),
    IS_READ("isRead"),
    ALARM_DATE("date"),
    EMPTY(null);

    private final String field;

    public Sort toSort(Sort.Direction direction) {
        return Sort.by(direction, field);
    }
}
