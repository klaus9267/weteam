package weteam.backend.domain.meeting_user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDto;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting_user.entity.MeetingUser;
import weteam.backend.domain.user.entity.User;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingUserService {
  private final MeetingUserRepository meetingUserRepository;

  public void acceptInvite(final Meeting meeting, final User user) {
    meeting.addMeetingUser(user);
  }

  public Meeting updateTimeSlot(final List<RequestTimeSlotDto> timeSlotDtoList, final long meetingId, final long userId) {
    this.validateTimeSlot(timeSlotDtoList);
    final MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, userId).orElseThrow(ErrorCode.MEETING_NOT_FOUND);
    meetingUser.updateTimeSlots(timeSlotDtoList);
    return meetingUser.getMeeting();
  }

  public void toggleDisplayed(final Long meetingId, final long userId) {
    final MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, userId).orElseThrow(ErrorCode.MEETING_NOT_FOUND);
    meetingUser.updateDisplayed();
  }

  public void quitMeeting(final long meetingId, final long userId) {
    final MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, userId)
        .orElseThrow(ErrorCode.NOT_FOUND);
    meetingUserRepository.delete(meetingUser);
  }

  private void validateTimeSlot(final List<RequestTimeSlotDto> timeSlotDtoList) {
    timeSlotDtoList.sort(Comparator.comparing(RequestTimeSlotDto::startedAt));

    for (int i = 0; i < timeSlotDtoList.size() - 1; i++) {
      if (timeSlotDtoList.get(i).endedAt().isAfter(timeSlotDtoList.get(i + 1).startedAt())) {
        throw new CustomException(ErrorCode.MEETING_TIME_DUPLICATE);
      }
    }
  }

  public long verifyAllChecked(final Meeting meeting) {
    return meeting.getMeetingUserList().stream()
        .filter(meetingUser -> meetingUser.getTimeSlotList().isEmpty())
        .count();
  }
}
