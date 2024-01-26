package weteam.backend.domain.meeting.dto;

import java.time.LocalDateTime;

public record CreateMeetingDto(
        String title,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Long projectId
) {
}
