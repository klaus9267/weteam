package weteam.backend.domain.alarm.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.project.entity.Project;

@Getter
@AllArgsConstructor
public enum AlarmType {
  PROJECT(Project.class),
  MEETING(Meeting.class);

  private final Class<?> entityType;
}
