package weteam.backend.domain.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDto;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.entity.MeetingUser;
import weteam.backend.domain.meeting.entity.TimeSlot;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.meeting.repository.MeetingUserRepository;
import weteam.backend.domain.meeting.repository.TimeSlotRepository;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingUserService {
  private final MeetingUserRepository meetingUserRepository;
  private final MeetingRepository meetingRepository;
  private final TimeSlotRepository timeSlotRepository;
  private final SecurityUtil securityUtil;

  @Transactional
  public String inviteUser(final Long meetingId) {
    final Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_MEETING));
    return meeting.getHashedId();
  }

  @Transactional
  public void acceptInvite(final String hashedId) {
    final Meeting meeting = meetingRepository.findByHashedId(hashedId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_MEETING));
    final MeetingUser meetingUser = MeetingUser.from(securityUtil.getCurrentUser(), meeting.getId());
    meeting.addMeetingUser(meetingUser);
  }

  @Transactional
  public void acceptInvite4Develop(final Long meetingId) {
    final Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_MEETING));
    final MeetingUser meetingUser = MeetingUser.from(securityUtil.getCurrentUser(), meeting.getId());
    meeting.addMeetingUser(meetingUser);
  }

  @Transactional
  public void updateTimeSlot(final List<RequestTimeSlotDto> timeSlotDtoList, final Long meetingId) {
    this.validateTimeSlot(timeSlotDtoList);
    final MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
    final List<TimeSlot> timeSlotList = TimeSlot.from(timeSlotDtoList, meetingUser);
    System.out.println(validaAllChecked(meetingUser.getMeeting()));
    timeSlotRepository.deleteAllByMeetingUser(meetingUser);
    timeSlotRepository.saveAll(timeSlotList);
  }

  private void validateTimeSlot(final List<RequestTimeSlotDto> timeSlotDtoList) {
    timeSlotDtoList.sort(Comparator.comparing(RequestTimeSlotDto::startedAt));

    for (int i = 0; i < timeSlotDtoList.size() - 1; i++) {
      if (timeSlotDtoList.get(i).endedAt().isAfter(timeSlotDtoList.get(i + 1).startedAt())) {
        throw new CustomException(CustomErrorCode.BAD_REQUEST, "겹치는 시간대가 있습니다");
      }
    }
  }

  private boolean validaAllChecked(final Meeting meeting) {
    final long count = meeting.getMeetingUserList().stream().filter(meetingUser -> meetingUser.getTimeSlotList().isEmpty()).count();
    return count > 0;
  }
}
