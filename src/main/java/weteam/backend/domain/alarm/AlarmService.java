package weteam.backend.domain.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.firebase.FirebaseService;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.alarm.entity.Alarm;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.common.pagination.param.AlarmPaginationParam;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {
  private final AlarmRepository alarmRepository;
  private final FirebaseService firebaseService;

  public Page<Alarm> readAlarmList(final AlarmPaginationParam paginationParam, final long userId) {
    return alarmRepository.findAllByUserId(paginationParam.toPageable(), userId);
  }

  @Transactional
  public <T> void addAlarmList(final T entity, final AlarmStatus status) {
    if (entity instanceof Project project) {
      this.sendAlarmList(Alarm.from(project, status));
    } else if (entity instanceof Meeting meeting) {
      this.sendAlarmList(Alarm.from(meeting, status));
    } else {
      throw new CustomException(ErrorCode.INVALID_DATA);
    }
  }

  @Transactional
  public <T> void addAlarmListWithTargetUser(final T entity, final AlarmStatus status, final User targetUser) {
    if (entity instanceof Project project) {
      this.sendAlarmList(Alarm.from(project, status, targetUser));
    } else if (entity instanceof Meeting meeting) {
      this.sendAlarmList(Alarm.from(meeting, status, targetUser));
    } else {
      throw new CustomException(ErrorCode.INVALID_DATA);
    }
  }

  @Transactional
  public <T> void addAlarmListWithTargetUserList(final T entity, final AlarmStatus status, final List<User> targerUserList) {
    final List<Alarm> alarmList = new ArrayList<>();

    if (entity instanceof Project project) {
      for (final User targetUser : targerUserList) {
        alarmList.addAll(Alarm.from(project, status, targetUser));
      }
    } else if (entity instanceof Meeting meeting) {
      for (final User targetUser : targerUserList) {
        alarmList.addAll(Alarm.from(meeting, status, targetUser));
      }
    } else {
      throw new CustomException(ErrorCode.INVALID_DATA);
    }

    this.sendAlarmList(alarmList);
  }

  @Transactional
  private void sendAlarmList(List<Alarm> alarmList) {
    alarmRepository.saveAll(alarmList);
    firebaseService.sendNotification(alarmList);
  }

  public void updateAlarmRead(final long alarmId, final long userId) {
    Alarm alarm = alarmRepository.findByIdAndUserId(alarmId, userId).orElseThrow(CustomException.raise(ErrorCode.NOT_FOUND));
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
