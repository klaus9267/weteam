package weteam.backend.domain.meeting.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDto;
import weteam.backend.domain.meeting_user.entity.MeetingUser;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "timeslots")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startedAt, endedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private MeetingUser meetingUser;

    private TimeSlot(final RequestTimeSlotDto timeSlotDto, final MeetingUser meetingUser) {
        this.startedAt = timeSlotDto.startedAt();
        this.endedAt = timeSlotDto.endedAt();
        this.meetingUser = meetingUser;
    }

    public static List<TimeSlot> from(final List<RequestTimeSlotDto> timeSlotDtoList, final MeetingUser meetingUser) {
        return timeSlotDtoList.stream().map(timeSlotDto -> new TimeSlot(timeSlotDto, meetingUser)).toList();
    }
}
