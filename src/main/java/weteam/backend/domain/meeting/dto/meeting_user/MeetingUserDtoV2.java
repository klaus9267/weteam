package weteam.backend.domain.meeting.dto.meeting_user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import weteam.backend.domain.meeting.entity.MeetingUser;
import weteam.backend.domain.user.dto.UserWithProfileImageDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class MeetingUserDtoV2 {
  private final Long id;
  private final UserWithProfileImageDto user;

  private MeetingUserDtoV2(final MeetingUser meetingUser) {
    this.id = meetingUser.getId();
    this.user = UserWithProfileImageDto.from(meetingUser.getUser());
  }

  public static List<MeetingUserDtoV2> from(final List<MeetingUser> meetingUser) {
    return meetingUser.stream().map(MeetingUserDtoV2::new).collect(Collectors.toList());
  }

  public static MeetingUserDtoV2 from(final MeetingUser meetingUser) {
    return new MeetingUserDtoV2(meetingUser);
  }
}
