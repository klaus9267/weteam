package weteam.backend.domain.project_user.param;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProjectRoleParam {
    @Parameter(example = "1")
    @NotNull
    private final Long projectId;

    @Parameter(example = "쩐주")
    @NotEmpty(message = "role is required")
    private final String role;
}
