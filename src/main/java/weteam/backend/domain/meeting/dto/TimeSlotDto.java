package weteam.backend.domain.meeting.dto;

import java.time.LocalDateTime;

public record TimeSlotDto(
        Long id,
        LocalDateTime startedAt,
        LocalDateTime endedAt
) {
}
