package weteam.backend.domain.bulk;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import weteam.backend.domain.bulk.repository.ProjectBulk;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/bulk")
@RequiredArgsConstructor
public class BulkController {
    private final ProjectBulk projectBulk;

    @PostMapping("/projects")
    @ResponseStatus(HttpStatus.CREATED)
    public void bulkProject(
            @Parameter(example = "100", description = "size^2 만큼 데이터가 저장됩니다. 100 입력시 10,000개 저장, 최대값 100")
            @Valid @Max(100)
            @RequestParam("size") final Integer size,
            @Parameter(example = "2023-10-01T00:00:00", description = "랜덤 날짜 시작일")
            @RequestParam("start") final LocalDateTime start,
            @Parameter(example = "2024-02-01T00:00:00", description = "랜덤 날짜 종료일")
            @RequestParam("end") final LocalDateTime end
    ) {
        projectBulk.bulkInsert(size, start, end);
    }
}
