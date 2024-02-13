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
import weteam.backend.domain.meeting.repository.MeetingUserRepository;
import weteam.backend.domain.meeting.repository.TimeSlotRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetingUserService {
  private final MeetingUserRepository meetingUserRepository;
  private final TimeSlotRepository timeSlotRepository;
  private final SecurityUtil securityUtil;
  
  @Transactional
  public void inviteMeeting(final Long meetingId, final Long userId) {
    if (meetingUserRepository.findByMeetingIdAndUserId(meetingId, userId).isPresent()) {
      throw new CustomException(CustomErrorCode.DUPLICATE, "이미 초대한 사용자입니다.");
    }
    final MeetingUser meetingUser = MeetingUser.from(userId, meetingId);
    meetingUserRepository.save(meetingUser);
  }
  
  @Transactional
  public void acceptMeeting(final Long meetingId) {
    MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND, "조회할 약속이 없습니다."));
    if (meetingUser.isAccept()) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, "이미 수락한 약속입니다.");
    }
    meetingUser.acceptMeeting();
  }
  
  @Transactional
  public void updateTimeSlot(final List<RequestTimeSlotDto> timeSlotDtoList, final Long meetingId) {
    this.validateTimeSlot(timeSlotDtoList);
    final MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
    final List<TimeSlot> timeSlotList = TimeSlot.from(timeSlotDtoList, meetingUser);
    timeSlotRepository.deleteAllByMeetingUser(meetingUser);
    timeSlotRepository.saveAll(timeSlotList);
  }
  
  private void validateTimeSlot(final List<RequestTimeSlotDto> timeSlotDtoList) {
    for (int i = 0; i < timeSlotDtoList.size(); i++) {
      for (int j = i + 1; j < timeSlotDtoList.size(); j++) {
        if (timeSlotDtoList.get(i).startedAt().isBefore(timeSlotDtoList.get(j).endedAt()) && timeSlotDtoList.get(j).startedAt().isBefore(timeSlotDtoList.get(i).endedAt())) {
          throw new CustomException(CustomErrorCode.BAD_REQUEST, "겹치는 시간대가 있습니다");
        }
      }
    }
  }
}
