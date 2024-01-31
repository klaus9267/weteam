package weteam.backend.domain.meeting.dto.meeting;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateMeetingDto(
        @NotEmpty(message = "title is required")
        String title,
        @NotNull
        LocalDateTime startedAt,
        @NotNull
        LocalDateTime endedAt
) {
}
