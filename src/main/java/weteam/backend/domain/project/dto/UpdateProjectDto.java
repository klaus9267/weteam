package weteam.backend.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateProjectDto(
    @NotBlank(message = "name is required")
    @Size(min = 1, max = 50)
    @Schema(example = "개재미없는 교양수업")
    String name,
    
    @NotNull(message = "startedAt is required")
    @Schema(example = "2023-12-24")
    LocalDate startedAt,
    
    @NotNull(message = "endedAt is required")
    @FutureOrPresent
    @Schema(example = "2024-12-24")
    LocalDate endedAt,
    
    @NotBlank(message = "explanation is required")
    @Size(min = 1, max = 50)
    @Schema(example = "1학점짜리 교양")
    String explanation
) {
}
