package weteam.backend.domain.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.common.pagination.param.MeetingPaginationParam;
import weteam.backend.domain.meeting.dto.meeting.*;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.entity.MeetingUser;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.repository.ProjectRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
  public MeetingDto addOne(final CreateMeetingDto meetingDto) {
    final Optional<Project> project = meetingDto.projectId() != null ? projectRepository.findById(meetingDto.projectId()) : Optional.empty();
    final Meeting meeting = project.map(p -> Meeting.from(meetingDto, securityUtil.getId(), p))
        .orElseGet(() -> Meeting.from(meetingDto, securityUtil.getId()));
    final Meeting addedMeeting = meetingRepository.save(meeting);

    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      byte[] encodedHash = digest.digest(Long.toString(addedMeeting.getId()).getBytes(StandardCharsets.UTF_8));
      StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
      for (byte b : encodedHash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      addedMeeting.addHashedId(hexString.toString());
    } catch (NoSuchAlgorithmException e) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, e.getMessage());
    }

    return MeetingDto.from(addedMeeting);
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
  public void acceptInvite(final String hashedProjectId) {
    final Meeting meeting = meetingRepository.findByHashedId(hashedProjectId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_MEETING));
    final MeetingUser meetingUser = MeetingUser.from(securityUtil.getId(), meeting.getId());
    meeting.addMeetingUser(meetingUser);
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
