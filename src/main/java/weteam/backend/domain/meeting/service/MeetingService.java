package weteam.backend.domain.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.AlarmService;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.common.HashUtil;
import weteam.backend.domain.common.pagination.param.MeetingPaginationParam;
import weteam.backend.domain.meeting.dto.meeting.*;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.entity.MeetingUser;
import weteam.backend.domain.meeting.entity.TimeSlot2;
import weteam.backend.domain.meeting.param.MeetingDetailParam;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.repository.ProjectRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MeetingService {
  private final MeetingRepository meetingRepository;
  private final AlarmService alarmService;
  private final ProjectRepository projectRepository;
  private final SecurityUtil securityUtil;

  public MeetingDetailDto readMeetingDetailDto(final Long meetingId) {
    final Meeting meeting = meetingRepository.findByIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND_MEETING));
    return MeetingDetailDto.from(meeting);
  }

  public MeetingDetailDtoV2 readMeetingDetailDtoV2(final MeetingDetailParam param) {
    final Meeting meeting = meetingRepository.findByIdAndUserId(param.getMeetingId(), securityUtil.getId()).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND_MEETING));
    Map<LocalDateTime, List<String>> timeMap = new HashMap<>();

    for (final MeetingUser meetingUser : meeting.getMeetingUserList()) {
      if (meetingUser.getTimeSlotList2().isEmpty()) continue;
      for (final TimeSlot2 timeSlot2 : meetingUser.getTimeSlotList2()) {
        timeMap.computeIfAbsent(timeSlot2.getTime(), list -> new ArrayList<>()).add(meetingUser.getUser().getUsername());
      }
    }
    timeMap.entrySet().removeIf(entry -> entry.getValue().size() < param.getMinimum());

    return MeetingDetailDtoV2.from(meeting, timeMap);
  }

  public MeetingPaginationDto readListWithPagination(final MeetingPaginationParam paginationParam) {
    final Page<Meeting> meetingPage = meetingRepository.findAllByUserId(paginationParam.toPageable(), securityUtil.getId());
    return MeetingPaginationDto.from(meetingPage);
  }

  @Transactional
  public MeetingDto addMeeting(final CreateMeetingDto meetingDto) {
    final Optional<Project> project = meetingDto.projectId() != null ? projectRepository.findById(meetingDto.projectId()) : Optional.empty();
    final Meeting meeting = project.map(p -> Meeting.from(meetingDto, securityUtil.getCurrentUser(), p))
        .orElseGet(() -> Meeting.from(meetingDto, securityUtil.getCurrentUser()));
    final Meeting addedMeeting = meetingRepository.save(meeting);

    project.ifPresent(value -> alarmService.addAlarmList(value, AlarmStatus.NEW_MEETING));

    final String hashedId = HashUtil.hashId(addedMeeting.getId());
    addedMeeting.addHashedId(hashedId);

    return MeetingDto.from(addedMeeting);
  }

  @Transactional
  public void updateMeeting(final UpdateMeetingDto meetingDto, final Long meetingId) {
    Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND_MEETING));
    if (!meeting.getHost().getId().equals(securityUtil.getId())) {
      throw new CustomException(CustomErrorCode.INVALID_HOST);
    }
    meeting.updateMeeting(meetingDto);
  }

  @Transactional
  public void acceptInvite(final String hashedProjectId) {
    final Meeting meeting = meetingRepository.findByHashedId(hashedProjectId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_MEETING));
    meeting.addMeetingUser(securityUtil.getCurrentUser());
  }

  @Transactional
  public void deleteMeeting(final Long meetingId) {
    final Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND_MEETING));
    if (!meeting.getHost().getId().equals(securityUtil.getId())) {
      throw new CustomException(CustomErrorCode.INVALID_HOST);
    }
    meetingRepository.delete(meeting);
  }
}
