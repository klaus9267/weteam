package weteam.backend.domain.meeting.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import weteam.backend.domain.meeting.entity.TimeSlot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class TimeSlotDto {
    private final Long id;
    private final LocalDateTime startedAt;
    private final LocalDateTime endedAt;

    private TimeSlotDto(final TimeSlot timeSlot) {
        this.id = timeSlot.getId();
        this.startedAt = timeSlot.getStartedAt();
        this.endedAt = timeSlot.getEndedAt();
    }

    public static List<TimeSlotDto> from(final List<TimeSlot> timeSlot) {
        return timeSlot.stream().map(TimeSlotDto::new).collect(Collectors.toList());
    }
}