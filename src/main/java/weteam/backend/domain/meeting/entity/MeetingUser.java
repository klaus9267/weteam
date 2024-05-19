package weteam.backend.domain.meeting.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDto;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDtoV2;
import weteam.backend.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "meeting_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MeetingUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private boolean isDisplayed;

  @ManyToOne(fetch = FetchType.LAZY)
  private Meeting meeting;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @OneToMany(mappedBy = "meetingUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TimeSlot> timeSlotList = new ArrayList<>();

  @OneToMany(mappedBy = "meetingUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TimeSlot2> timeSlotList2 = new ArrayList<>();

  private MeetingUser(final User user, final Meeting meeting) {
    this.meeting = meeting;
    this.isDisplayed = true;
    this.user = user;
  }

  public static MeetingUser from(final User user, final Meeting meeting) {
    return new MeetingUser(user, meeting);
  }

  public void updateTimeSlots(final List<RequestTimeSlotDto> timeSlotDtoList) {
    final List<TimeSlot> timeSlot2List = TimeSlot.from(timeSlotDtoList, this);
    this.timeSlotList.clear();
    this.timeSlotList.addAll(timeSlot2List);
  }

  public void updateTimeSlotsV2(final RequestTimeSlotDtoV2 timeSlotDtoV2) {
    final List<TimeSlot2> timeSlot2List = TimeSlot2.from(timeSlotDtoV2, this);
    this.timeSlotList2.clear();
    this.timeSlotList2.addAll(timeSlot2List);
  }

  public void quit() {
    this.isDisplayed = false;
  }
}
