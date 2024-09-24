package weteam.backend.domain.alarm.strategy;

import weteam.backend.domain.alarm.entity.Alarm;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.user.entity.User;

import java.util.List;

public class MeetingAlarmStrategy implements AlarmStrategy<Meeting> {
  @Override
  public AlarmType alarmType() {
    return AlarmType.MEETING;
  }

  @Override
  public List<Alarm> createAlarms(Meeting meeting, AlarmStatus status) {
    return Alarm.from(meeting, status);
  }

  @Override
  public List<Alarm> createAlarms(Meeting meeting, AlarmStatus status, User targetUser) {
    return Alarm.from(meeting, status, targetUser);
  }

  @Override
  public List<Alarm> createAlarms(Meeting meeting, AlarmStatus status, List<User> targetUsers) {
    return targetUsers.stream()
        .flatMap(user -> Alarm.from(meeting, status, user).stream())
        .toList();
  }
}
