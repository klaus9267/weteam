package weteam.backend.domain.meeting.dto.meeting_user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import weteam.backend.domain.meeting.dto.time_slot.TimeSlotDto;
import weteam.backend.domain.meeting.entity.MeetingUser;
import weteam.backend.domain.project.dto.ProjectUserDto;
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class MeetingUserDto {
  private final Long id;
  private final UserDto user;
  private final List<TimeSlotDto> timeSlotList;
  
  private MeetingUserDto(final MeetingUser meetingUser) {
    this.id = meetingUser.getId();
    this.user = UserDto.from(meetingUser.getUser());
    this.timeSlotList = TimeSlotDto.from(meetingUser.getTimeSlotList());
  }
  
  public static List<MeetingUserDto> from(final List<MeetingUser> meetingUser) {
    return meetingUser.stream().map(MeetingUserDto::new).collect(Collectors.toList());
  }
}
