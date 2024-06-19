package weteam.backend.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateProjectDto(
    @NotBlank(message = "name is required")
    @Size(min = 1, max = 50)
    @Schema(example = "개재미없는 교양수업")
    String name,

    @NotNull(message = "startedAt is required")
    @Schema(example = "2023-12-24")
    @PastOrPresent
    LocalDate startedAt,

    @NotNull
    @Schema(example = "1L")
    Long imageId,

    @NotNull(message = "endedAt is required")
    @Schema(example = "2025-12-24")
    @FutureOrPresent
    LocalDate endedAt,

    @NotBlank(message = "explanation is required")
    @Size(min = 1, max = 50)
    @Schema(example = "1학점짜리 교양")
    String explanation
) {
}
