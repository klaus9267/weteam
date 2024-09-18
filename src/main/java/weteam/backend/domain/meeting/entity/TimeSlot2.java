package weteam.backend.domain.meeting.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDtoV2;
import weteam.backend.domain.meeting_user.entity.MeetingUser;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "timeslots2")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TimeSlot2 {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private LocalDateTime time;

  @ManyToOne(fetch = FetchType.LAZY)
  private MeetingUser meetingUser;

  private TimeSlot2(final LocalDateTime time, final MeetingUser meetingUser) {
    this.time = time;
    this.meetingUser = meetingUser;
  }

  public static List<TimeSlot2> from(final RequestTimeSlotDtoV2 timeSlotDtoV2, final MeetingUser meetingUser) {
    return timeSlotDtoV2.timeList().stream().map(time -> new TimeSlot2(time, meetingUser)).toList();
  }
}
