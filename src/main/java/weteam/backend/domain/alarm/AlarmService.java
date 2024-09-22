package weteam.backend.domain.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import weteam.backend.application.firebase.FirebaseService;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.alarm.entity.Alarm;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.alarm.factory.AlarmStrategy;
import weteam.backend.domain.alarm.factory.AlarmFactory;
import weteam.backend.domain.common.pagination.param.AlarmPaginationParam;
import weteam.backend.domain.user.entity.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {
  private final AlarmRepository alarmRepository;
  private final AlarmFactory alarmFactory;
  private final FirebaseService firebaseService;

  public Page<Alarm> readAlarmList(final AlarmPaginationParam paginationParam, final long userId) {
    return alarmRepository.findAllByUserId(paginationParam.toPageable(), userId);
  }

  public <T> void addAlarms(final T entity, final AlarmStatus status) {
    final AlarmStrategy<T> alarmStrategy = alarmFactory.getCreator(entity.getClass().getName());
    final List<Alarm> alarms = alarmStrategy.createAlarms(entity, status);
    sendAlarmList(alarms);
  }

  public <T> void addAlarms(final T entity, final AlarmStatus status, final User targetUser) {
    final AlarmStrategy<T> alarmStrategy = alarmFactory.getCreator(entity.getClass().getName());
    final List<Alarm> alarms = alarmStrategy.createAlarms(entity, status, targetUser);
    sendAlarmList(alarms);
  }

  public <T> void addAlarms(final T entity, final AlarmStatus status, final List<User> targetUsers) {
    final AlarmStrategy<T> alarmStrategy = alarmFactory.getCreator(entity.getClass().getName());
    final List<Alarm> alarms = alarmStrategy.createAlarms(entity, status, targetUsers);
    sendAlarmList(alarms);
  }

  private void sendAlarmList(List<Alarm> alarmList) {
    alarmRepository.saveAll(alarmList);
    firebaseService.sendNotification(alarmList);
  }

  public void updateAlarmRead(final long alarmId, final long userId) {
    Alarm alarm = alarmRepository.findByIdAndUserId(alarmId, userId).orElseThrow(ErrorCode.NOT_FOUND);
    if (!alarm.isRead()) {
      alarm.markAsRead();
    }
  }

  public void updateAllRead(final long userId) {
    alarmRepository.findAllByUserId(userId).stream()
        .filter(alarm -> !alarm.isRead())
        .forEach(Alarm::markAsRead);
  }
}
