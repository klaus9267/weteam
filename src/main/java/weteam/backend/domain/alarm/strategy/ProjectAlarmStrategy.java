package weteam.backend.domain.alarm.strategy;

import org.springframework.stereotype.Component;
import weteam.backend.domain.alarm.entity.Alarm;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.entity.User;

import java.util.List;

@Component
public class ProjectAlarmStrategy implements AlarmStrategy<Project> {
  @Override
  public AlarmType alarmType() {
    return AlarmType.PROJECT;
  }

  @Override
  public List<Alarm> createAlarms(Project project, AlarmStatus status) {
    return Alarm.from(project, status);
  }

  @Override
  public List<Alarm> createAlarms(Project project, AlarmStatus status, User targetUser) {
    return Alarm.from(project, status, targetUser);
  }

  @Override
  public List<Alarm> createAlarms(Project project, AlarmStatus status, List<User> targetUsers) {
    return targetUsers.stream()
        .flatMap(user -> Alarm.from(project, status, user).stream())
        .toList();
  }
}
