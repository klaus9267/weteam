package weteam.backend.domain.meeting_user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.domain.alarm.AlarmService;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.meeting.MeetingService;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDto;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.user.UserService;
import weteam.backend.domain.user.entity.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MeetingUserFacade {
  private final MeetingUserService meetingUserService;
  private final MeetingService meetingService;
  private final UserService userService;
  private final AlarmService alarmService;
  private final SecurityUtil securityUtil;

  @Transactional(readOnly = true)
  public String findMeetingHashedId(final long meetingId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    return meetingService.findMeeting(meetingId, currentUserId).getHashedId();
  }

  @Transactional
  public void acceptInvite(final String hashedMeetingId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final User user = userService.findUser(currentUserId);
    final Meeting meeting = meetingService.findMeeting(hashedMeetingId);
    meetingUserService.acceptInvite(meeting, user);
  }

  @Transactional
  public void updateTime(final long meetingId, final List<RequestTimeSlotDto> timeSlotDtos) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Meeting meeting = meetingUserService.updateTimeSlot(timeSlotDtos, meetingId, currentUserId);

    final long count = meetingUserService.verifyAllChecked(meeting);
    final AlarmStatus status = count == 0 && meeting.isDone() ? AlarmStatus.ALL_CHECKED : AlarmStatus.TIME_UPDATE;
    alarmService.addAlarms(meeting, status);
  }

  @Transactional
  public void toggleDisplayed(final long meetingId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    meetingUserService.toggleDisplayed(meetingId, currentUserId);
  }

  @Transactional
  public void quitMeeting(final long meetingId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    meetingUserService.quitMeeting(meetingId, currentUserId);
  }
}
