package weteam.backend.domain.project.param;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProjectRoleParam {
    @Parameter(example = "1")
    private final Long projectId;

    @Parameter(example = "쩐주")
    private final String role;
}
