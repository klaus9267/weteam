package weteam.backend.domain.alarm.strategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.project.entity.Project;

@Getter
@AllArgsConstructor
public enum AlarmType {
  PROJECT(Project.class),
  MEETING(Meeting.class);

  private final Class<?> entityType;
}
