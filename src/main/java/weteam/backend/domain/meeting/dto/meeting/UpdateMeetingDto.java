package weteam.backend.domain.meeting.dto.meeting;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import weteam.backend.domain.common.annotation.AtLeastOneNotNull;

import java.time.LocalDateTime;

@AtLeastOneNotNull
public record UpdateMeetingDto(
    @Schema(example = "지하철 무임승차 빌런 발견")
    String title,
    
    @Schema(example = "2024-02-05T10:00:00", type = "string")
    LocalDateTime startedAt,
    @Schema(example = "2024-02-05T10:00:00", type = "string")
    LocalDateTime endedAt
) {
}
