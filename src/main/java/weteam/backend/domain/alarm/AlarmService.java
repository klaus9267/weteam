package weteam.backend.domain.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.firebase.FirebaseService;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.dto.AlarmPaginationDto;
import weteam.backend.domain.alarm.entity.Alarm;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.common.pagination.param.AlarmPaginationParam;
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
  public void addList(final Project project, final AlarmStatus status) {
    final List<Alarm> alarmList = Alarm.from(project, status,null);
    alarmRepository.saveAll(alarmList);
    firebaseService.sendNotification(alarmList);
  }

//  @Transactional
//  public <T> void addAlarmList(final T entity, final AlarmStatus status, final User targerUser) {
//    if (entity instanceof Project project) {
//      addAlarmListFromProject(project, status, targerUser);
//    } else if (entity instanceof Meeting meeting) {
//      createAlarmsFromMeeting(meeting, status);
//    } else {
//      throw new CustomException(CustomErrorCode.BAD_REQUEST, "잘못된 형식의 데이터입니다.");
//    }
//  }

  private void addAlarmListFromProject(final Project project, final AlarmStatus status, final User targerUser) {
    final List<Alarm> alarmList = Alarm.from(project, status, targerUser);
    alarmRepository.saveAll(alarmList);
    firebaseService.sendNotification(alarmList);
  }

  @Transactional
  public void addListWithTargetUser(final Project project, final AlarmStatus status, final User targetUser) {
    final List<Alarm> alarmList = Alarm.from(project, status, targetUser);
    alarmRepository.saveAll(alarmList);
    firebaseService.sendNotification(alarmList);
  }

  @Transactional
  public void addListWithTargetUserList(final Project project, final AlarmStatus status, final List<User> targetUserList) {
    final List<Alarm> newAlarmList = new ArrayList<>();
    for (final User user : targetUserList) {
      newAlarmList.addAll(Alarm.from(project, status, user));
    }

    alarmRepository.saveAll(newAlarmList);
    firebaseService.sendNotification(newAlarmList);
  }

  @Transactional
  public void updateIsRead(final Long alarmId, final Long userId) {
    Alarm alarm = alarmRepository.findByIdAndUserId(alarmId, userId).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND));
    if (alarm.isRead()) {
      throw new CustomException(CustomErrorCode.DUPLICATE, "읽은 알람입니다.");
    }
    alarm.changeIsRead();
  }

  @Transactional
  public void updateAllIsRead() {
    List<Alarm> alarmList = alarmRepository.findAllByUserId(securityUtil.getId()).stream().filter(alarm -> !alarm.isRead()).toList();
    if (alarmList.isEmpty()) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, "미확인 알람이 없습니다.");
    } else {
      alarmList.forEach(Alarm::changeIsRead);
    }
  }
}
