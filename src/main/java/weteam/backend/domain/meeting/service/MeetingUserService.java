package weteam.backend.domain.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDto;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDtoV2;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.entity.MeetingUser;
import weteam.backend.domain.meeting.entity.TimeSlot;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.meeting.repository.MeetingUserRepository;
import weteam.backend.domain.meeting.repository.TimeSlot2Repository;
import weteam.backend.domain.meeting.repository.TimeSlotRepository;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingUserService {
  private final MeetingUserRepository meetingUserRepository;
  private final MeetingRepository meetingRepository;
  private final TimeSlotRepository timeSlotRepository;
  private final TimeSlot2Repository timeSlot2Repository;
  private final SecurityUtil securityUtil;

  @Transactional
  public String inviteUser(final Long meetingId) {
    final Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_MEETING));
    return meeting.getHashedId();
  }

  @Transactional
  public void acceptInvite(final String hashedId) {
    final Meeting meeting = meetingRepository.findByHashedId(hashedId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_MEETING));
    meeting.addMeetingUser(securityUtil.getCurrentUser());
  }

  @Transactional
  public void updateTimeSlot(final List<RequestTimeSlotDto> timeSlotDtoList, final Long meetingId) {
    this.validateTimeSlot(timeSlotDtoList);
    final MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
    final List<TimeSlot> timeSlotList = TimeSlot.from(timeSlotDtoList, meetingUser);

    timeSlotRepository.deleteAllByMeetingUser(meetingUser);
    timeSlotRepository.saveAll(timeSlotList);
  }

  @Transactional
  public void updateTimeSlotV2(final RequestTimeSlotDtoV2 timeSlotDtoV2, final Long meetingId) {
    final MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
    timeSlot2Repository.deleteAllByMeetingUserId(meetingUser.getId());
    meetingUser.updateTimeSlots(timeSlotDtoV2);
    this.verifyAllChecked(meetingUser.getMeeting());
  }

  @Transactional
  public void quitMeeting(final Long meetingId) {
    final MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
    meetingUser.quit();
  }

  private void validateTimeSlot(final List<RequestTimeSlotDto> timeSlotDtoList) {
    timeSlotDtoList.sort(Comparator.comparing(RequestTimeSlotDto::startedAt));

    for (int i = 0; i < timeSlotDtoList.size() - 1; i++) {
      if (timeSlotDtoList.get(i).endedAt().isAfter(timeSlotDtoList.get(i + 1).startedAt())) {
        throw new CustomException(CustomErrorCode.BAD_REQUEST, "겹치는 시간대가 있습니다");
      }
    }
  }

  @Transactional
  private void verifyAllChecked(final Meeting meeting) {
    final long count = meeting.getMeetingUserList().stream().filter(meetingUser -> meetingUser.getTimeSlotList2().isEmpty()).count();
    if (count == 0) meeting.done();
  }
}
