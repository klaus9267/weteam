package weteam.backend.domain.project.param;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateHostParam {
    @Parameter(example = "1")
    private final Long projectId;

    @Parameter(description = "새로운 hostId", example = "2")
    private final Long userId;
}
