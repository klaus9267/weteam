package weteam.backend.domain.alarm.strategy;

import weteam.backend.domain.alarm.entity.Alarm;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.user.entity.User;

import java.util.List;

public interface AlarmStrategy<T> {
  AlarmType alarmType();

  List<Alarm> createAlarms(T entity, AlarmStatus status);

  List<Alarm> createAlarms(T entity, AlarmStatus status, User targetUser);

  List<Alarm> createAlarms(T entity, AlarmStatus status, List<User> targetUsers);
}
