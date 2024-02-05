package weteam.backend.domain.meeting.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record RequestTimeSlotDto(
        @Schema(example = "2024-02-05T10:00:00", type = "string")
        LocalDateTime startedAt,
        @Schema(example = "2024-02-05T12:00:00", type = "string")
        LocalDateTime endedAt
) {
}
