package weteam.backend.domain.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.firebase.FirebaseService;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.alarm.dto.AlarmPaginationDto;
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
  private final SecurityUtil securityUtil;
  private final FirebaseService firebaseService;

  public AlarmPaginationDto readAlarmList(final AlarmPaginationParam paginationParam) {
    final Page<Alarm> alarmPage = alarmRepository.findAllByUserId(paginationParam.toPageable(), securityUtil.getId());
    return AlarmPaginationDto.from(alarmPage);
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

  @Transactional
  public void updateIsRead(final Long alarmId, final Long userId) {
    Alarm alarm = alarmRepository.findByIdAndUserId(alarmId, userId).orElseThrow(CustomException.raise(ErrorCode.NOT_FOUND));
    if (alarm.isRead()) {
      alarm.markAsRead();
    }
  }

  @Transactional
  public void updateAllIsRead() {
    List<Alarm> alarmList = alarmRepository.findAllByUserId(securityUtil.getId()).stream().filter(alarm -> !alarm.isRead()).toList();
    if (alarmList.isEmpty()) {
      throw new CustomException(ErrorCode.NOT_EXIST_UNREAD_ALARM);
    } else {
      alarmList.forEach(Alarm::markAsRead);
    }
  }
}
