package weteam.backend.domain.alarm.dto;

import lombok.Getter;
import weteam.backend.domain.alarm.entity.Alarm;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.meeting.dto.meeting.MeetingDto;
import weteam.backend.domain.project.dto.ProjectDto;
import weteam.backend.domain.user.dto.UserDto;

import java.time.LocalDate;
import java.util.List;

@Getter
public class AlarmDto {
  private final Long id;
  private final boolean isRead;
  private final AlarmStatus status;
  private final LocalDate date;
  private final UserDto targetUser;
  private final UserDto user;
  private final ProjectDto project;
  private final MeetingDto meeting;

  public AlarmDto(final Alarm alarm) {
    this.id = alarm.getId();
    this.isRead = alarm.isRead();
    this.user = UserDto.from(alarm.getUser());
    this.targetUser = alarm.getTargetUser() == null ? null : UserDto.from(alarm.getTargetUser());
    this.project = alarm.getProject() != null ? ProjectDto.from(alarm.getProject()) : null;
    this.meeting = alarm.getMeeting() != null ? MeetingDto.from(alarm.getMeeting()) : null;
    this.status = alarm.getStatus();
    this.date = LocalDate.from(alarm.getDate());
  }

  public static List<AlarmDto> from(final List<Alarm> alarmList) {
    return alarmList.stream().map(AlarmDto::new).toList();
  }
}
