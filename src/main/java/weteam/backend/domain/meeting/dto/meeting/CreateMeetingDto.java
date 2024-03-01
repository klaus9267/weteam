package weteam.backend.domain.meeting.dto.meeting;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateMeetingDto(
    @NotEmpty(message = "title is required")
    @Schema(example = "성현이 말년 휴가 파티")
    String title,
    @NotNull
    @Schema(example = "9999-12-31T23:59:59", type = "string")
    LocalDateTime startedAt,
    
    @NotNull
    @Schema(example = "1L")
    Integer imageId,
    @NotNull
    @Schema(example = "9999-12-31T23:59:59", type = "string")
    LocalDateTime endedAt,
    @Schema(example = "1", nullable = true)
    Long projectId
) {
}
