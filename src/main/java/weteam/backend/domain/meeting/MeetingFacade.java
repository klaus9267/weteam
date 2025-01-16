package weteam.backend.domain.meeting;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.domain.alarm.AlarmService;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.common.pagination.param.MeetingPaginationParam;
import weteam.backend.domain.meeting.dto.meeting.*;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.project.ProjectService;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.UserService;
import weteam.backend.domain.user.entity.User;

@Component
@RequiredArgsConstructor
public class MeetingFacade {
  private final MeetingService meetingService;
  private final ProjectService projectService;
  private final UserService userService;
  private final SecurityUtil securityUtil;
  private final AlarmService alarmService;

  @Transactional
  public MeetingDto addMeeting(final CreateMeetingDto createMeetingDto) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final User currentUser = userService.findUser(currentUserId);
    final Project project = createMeetingDto.projectId() == null ? null : projectService.findProject(createMeetingDto.projectId());
    final Meeting meeting = meetingService.addMeeting(createMeetingDto, currentUser, project);

    if (project != null) {
      alarmService.addAlarms(project, AlarmStatus.NEW_MEETING);
    }

    return MeetingDto.from(meeting);
  }

  @Transactional(readOnly = true)
  public MeetingDetailDto findMeeting(final long meetingId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Meeting meeting = meetingService.findMeeting(meetingId, currentUserId);
    return MeetingDetailDto.from(meeting);
  }

  @Transactional(readOnly = true)
  public MeetingPaginationDto pagingMeetings(final MeetingPaginationParam meetingPaginationParam) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Page<Meeting> meetingPage = meetingService.pagingMeetings(meetingPaginationParam, currentUserId);
    return MeetingPaginationDto.from(meetingPage);
  }

  @Transactional
  public void updateMeeting(final long meetingId, final UpdateMeetingDto updateMeetingDto) {
    final long currentUserId = securityUtil.getCurrentUserId();
    meetingService.updateMeeting(updateMeetingDto, meetingId, currentUserId);
  }

  @Transactional
  public void deleteMeeting(final long meetingId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    meetingService.deleteMeeting(meetingId, currentUserId);
  }
}
