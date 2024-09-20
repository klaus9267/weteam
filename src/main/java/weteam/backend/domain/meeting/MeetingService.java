package weteam.backend.domain.meeting;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.common.HashUtil;
import weteam.backend.domain.common.pagination.param.MeetingPaginationParam;
import weteam.backend.domain.meeting.dto.meeting.CreateMeetingDto;
import weteam.backend.domain.meeting.dto.meeting.UpdateMeetingDto;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class MeetingService {
  private final MeetingRepository meetingRepository;

  @Transactional
  public Meeting addMeeting(final CreateMeetingDto createMeetingDto, final User user, final Project project) {
    final Meeting newMeeting = project == null
        ? Meeting.from(createMeetingDto, user)
        : Meeting.from(createMeetingDto, user, project);
    final Meeting savedMeeting = meetingRepository.save(newMeeting);

    final String hashedId = HashUtil.hashId(savedMeeting.getId());
    savedMeeting.addHashedId(hashedId);

    return meetingRepository.save(savedMeeting);
  }

  public Meeting findMeeting(final long meetingId, final long userId) {
    return meetingRepository.findByIdAndUserId(meetingId, userId).orElseThrow(ErrorCode.MEETING_NOT_FOUND);
  }

  public Meeting findMeeting(final String hashedMeetingId) {
    return meetingRepository.findByHashedId(hashedMeetingId).orElseThrow(ErrorCode.MEETING_NOT_FOUND);
  }

  public Page<Meeting> pagingMeetings(final MeetingPaginationParam paginationParam, final long userId) {
    return meetingRepository.findAllByUserId(paginationParam.toPageable(), userId);
  }

  @Transactional
  public void updateMeeting(final UpdateMeetingDto meetingDto, final long meetingId, final long userId) {
    final Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(ErrorCode.MEETING_NOT_FOUND);
    if (!meeting.getHost().getId().equals(userId)) {
      throw new CustomException(ErrorCode.INVALID_HOST);
    }
    meeting.updateMeeting(meetingDto);
  }

  @Transactional
  public void acceptInvite(final String hashedMeetingId, final User user) {
    final Meeting meeting = meetingRepository.findByHashedId(hashedMeetingId).orElseThrow(ErrorCode.MEETING_NOT_FOUND);
    meeting.addMeetingUser(user);
  }

  @Transactional
  public void deleteMeeting(final Long meetingId, final long userId) {
    final Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(ErrorCode.MEETING_NOT_FOUND);
    if (!meeting.getHost().getId().equals(userId)) {
      throw new CustomException(ErrorCode.INVALID_HOST);
    }
    meetingRepository.delete(meeting);
  }
}
