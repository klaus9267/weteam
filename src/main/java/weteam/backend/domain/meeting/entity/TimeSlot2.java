package weteam.backend.domain.meeting.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  public static List<TimeSlot2> from(final List<LocalDateTime> timeList, final MeetingUser meetingUser) {
    return timeList.stream().map(time -> new TimeSlot2(time, meetingUser)).toList();
  }
}
