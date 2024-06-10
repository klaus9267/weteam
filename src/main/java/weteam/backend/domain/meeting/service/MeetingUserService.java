package weteam.backend.domain.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.alarm.AlarmService;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDto;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDtoV2;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.entity.MeetingUser;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.meeting.repository.MeetingUserRepository;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingUserService {
  private final MeetingUserRepository meetingUserRepository;
  private final MeetingRepository meetingRepository;
  private final AlarmService alarmService;
  private final SecurityUtil securityUtil;

  @Transactional
  public String inviteUser(final Long meetingId) {
    final Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(CustomException.raise(ErrorCode.MEETING_NOT_FOUND));
    return meeting.getHashedId();
  }

  @Transactional
  public void acceptInvite(final String hashedId) {
    final Meeting meeting = meetingRepository.findByHashedId(hashedId).orElseThrow(CustomException.raise(ErrorCode.MEETING_NOT_FOUND));
    meeting.addMeetingUser(securityUtil.getCurrentUser());
  }

  @Transactional
  public void updateTimeSlot(final List<RequestTimeSlotDto> timeSlotDtoList, final Long meetingId) {
    this.validateTimeSlot(timeSlotDtoList);
    final MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(CustomException.raise(ErrorCode.MEETING_NOT_FOUND));
    meetingUser.updateTimeSlots(timeSlotDtoList);
    this.verifyAllChecked(meetingUser.getMeeting());
  }

  @Transactional
  public void updateTimeSlotV2(final RequestTimeSlotDtoV2 timeSlotDtoV2, final Long meetingId) {
    final MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(CustomException.raise(ErrorCode.MEETING_NOT_FOUND));
    meetingUser.updateTimeSlotsV2(timeSlotDtoV2);
    this.verifyAllCheckedV2(meetingUser.getMeeting());
  }

  @Transactional
  public void updateMeetingDisplayed(final Long meetingId) {
    final MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(CustomException.raise(ErrorCode.MEETING_NOT_FOUND));
    meetingUser.updateDisplayed();
  }

  @Transactional
  public void quitMeeting(final Long meetingId) {
    final Meeting meeting = meetingRepository.findByIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(CustomException.raise(ErrorCode.MEETING_NOT_FOUND));
    meeting.quit(securityUtil.getId());
  }

  private void validateTimeSlot(final List<RequestTimeSlotDto> timeSlotDtoList) {
    timeSlotDtoList.sort(Comparator.comparing(RequestTimeSlotDto::startedAt));

    for (int i = 0; i < timeSlotDtoList.size() - 1; i++) {
      if (timeSlotDtoList.get(i).endedAt().isAfter(timeSlotDtoList.get(i + 1).startedAt())) {
        throw new CustomException(ErrorCode.MEETING_TIME_DUPLICATE);
      }
    }
  }

  @Transactional
  private void verifyAllCheckedV2(final Meeting meeting) {
    final long count = meeting.getMeetingUserList().stream().filter(meetingUser -> meetingUser.getTimeSlotList2().isEmpty()).count();
    if (count == 0) {
      if (meeting.isDone()) {
        alarmService.addAlarmList(meeting, AlarmStatus.ALL_CHECKED);
        meeting.done();
      } else {
        alarmService.addAlarmList(meeting, AlarmStatus.TIME_UPDATE);
      }
    }
  }

  @Transactional
  private void verifyAllChecked(final Meeting meeting) {
    final long count = meeting.getMeetingUserList().stream().filter(meetingUser -> meetingUser.getTimeSlotList().isEmpty()).count();
    if (count == 0) meeting.done();
  }
}
