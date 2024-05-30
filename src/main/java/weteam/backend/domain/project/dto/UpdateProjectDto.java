package weteam.backend.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import weteam.backend.domain.common.annotation.AtLeastOneNotNull;

import java.time.LocalDate;

@AtLeastOneNotNull
public record UpdateProjectDto(
    @Size(min = 1, max = 50)
    String name,

    @Schema(example = "2023-12-24")
    LocalDate startedAt,

    @FutureOrPresent
    @Schema(example = "2024-12-24")
    LocalDate endedAt,

    @Size(min = 1, max = 50)
    @Schema(example = "1학점짜리 교양")
    String explanation
) {
}
