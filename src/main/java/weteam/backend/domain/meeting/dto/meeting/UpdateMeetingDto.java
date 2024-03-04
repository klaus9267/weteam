package weteam.backend.domain.meeting.dto.meeting;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateMeetingDto(
    @NotEmpty(message = "title is required")
    @Schema(example = "지하철 무임승차 빌런 발견")
    String title,
    
    @NotNull
    @Schema(example = "2024-02-05T10:00:00", type = "string")
    LocalDateTime startedAt,
    @NotNull
    @Schema(example = "2024-02-05T10:00:00", type = "string")
    LocalDateTime endedAt
) {
}
