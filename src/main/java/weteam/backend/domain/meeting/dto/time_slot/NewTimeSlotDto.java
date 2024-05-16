package weteam.backend.domain.meeting.dto.time_slot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import weteam.backend.domain.meeting.entity.TimeSlot2;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class NewTimeSlotDto {
  private final Long id;
  private final LocalDateTime time;

  private NewTimeSlotDto(final TimeSlot2 timeSlot2) {
    this.id = timeSlot2.getId();
    this.time = timeSlot2.getTime();
  }

  public static NewTimeSlotDto from(final TimeSlot2 timeSlot2) {
    return new NewTimeSlotDto(timeSlot2);
  }

  public static List<NewTimeSlotDto> from(final List<TimeSlot2> timeSlot2List) {
    return timeSlot2List.stream().map(NewTimeSlotDto::new).toList();
  }
}
