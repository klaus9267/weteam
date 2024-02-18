package weteam.backend.domain.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.common.pagination.param.MeetingPaginationParam;
import weteam.backend.domain.meeting.dto.meeting.CreateMeetingDto;
import weteam.backend.domain.meeting.dto.meeting.MeetingDetailDto;
import weteam.backend.domain.meeting.dto.meeting.MeetingPaginationDto;
import weteam.backend.domain.meeting.dto.meeting.UpdateMeetingDto;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.repository.ProjectRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetingService {
  private final MeetingRepository meetingRepository;
  private final ProjectRepository projectRepository;
  private final SecurityUtil securityUtil;
  
  public MeetingDetailDto readOne(final Long meetingId) {
    final Meeting meeting = meetingRepository.findByIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND_MEETING));
    return MeetingDetailDto.from(meeting);
  }
  
  public MeetingPaginationDto readListWithPagination(final MeetingPaginationParam paginationParam) {
    final Page<Meeting> meetingPage = meetingRepository.findAllByUserId(paginationParam.toPageable(), securityUtil.getId());
    return MeetingPaginationDto.from(meetingPage);
  }
  
  @Transactional
  public void addOne(final CreateMeetingDto meetingDto) {
    Optional<Project> project = meetingDto.projectId() != null ? projectRepository.findById(meetingDto.projectId()) : Optional.empty();
    Meeting meeting = project.map(p -> Meeting.from(meetingDto, securityUtil.getId(), p))
                             .orElseGet(() -> Meeting.from(meetingDto, securityUtil.getId()));
    meetingRepository.save(meeting);
  }
  
  
  @Transactional
  public void updateOne(final UpdateMeetingDto meetingDto, final Long meetingId) {
    Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND_MEETING));
    if (!meeting.getHost().getId().equals(securityUtil.getId())) {
      throw new CustomException(CustomErrorCode.INVALID_HOST);
    }
    meeting.updateMeeting(meetingDto);
  }
  
  @Transactional
  public void deleteOne(final Long meetingId) {
    final Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND_MEETING));
    if (!meeting.getHost().getId().equals(securityUtil.getId())) {
      throw new CustomException(CustomErrorCode.INVALID_HOST);
    }
    meetingRepository.delete(meeting);
  }
}
