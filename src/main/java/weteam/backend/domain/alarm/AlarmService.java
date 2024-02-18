package weteam.backend.domain.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.dto.AlarmPaginationDto;
import weteam.backend.domain.common.pagination.param.AlarmPaginationParam;
import weteam.backend.domain.firebase.FirebaseService;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.repository.ProjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {
  private final AlarmRepository alarmRepository;
  private final ProjectRepository projectRepository;
  private final SecurityUtil securityUtil;
  private final FirebaseService firebaseService;
  
  public AlarmPaginationDto readAlarmList(final AlarmPaginationParam paginationParam) {
    final Page<Alarm> alarmPage = alarmRepository.findAllByUserId(paginationParam.toPageable(), securityUtil.getId());
    return AlarmPaginationDto.from(alarmPage);
  }
  
  @Transactional
  public void addList(final Project project, final AlarmStatus status) {
    final List<Alarm> alarmList = Alarm.from(project, status);
    alarmRepository.saveAll(alarmList);
    firebaseService.sendNotification(alarmList);
    
  }
  
  @Transactional
  public void addListWithTargetUser(final Project project, final AlarmStatus status, final Long targetUserId) {
    final List<Alarm> alarmList = Alarm.from(project, status, targetUserId);
    alarmRepository.saveAll(alarmList);
    firebaseService.sendNotification(alarmList);
  }
  
  @Transactional
  public void updateOneRead(final Long alarmId, final Long userId) {
    Alarm alarm = alarmRepository.findByIdAndUserId(alarmId, userId).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND));
    if (alarm.isRead()) {
      throw new CustomException(CustomErrorCode.DUPLICATE, "읽은 알람입니다.");
    }
    alarm.changeIsRead();
  }
  
  @Transactional
  public void updateAllRead(final Long userId) {
    //TODO: query 튜닝
    List<Alarm> alarmList = alarmRepository.findAllByUserId(userId).stream().filter(alarm -> !alarm.isRead()).toList();
    if (alarmList.isEmpty()) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, "미확인 알람이 없습니다.");
    } else {
      alarmList.forEach(Alarm::changeIsRead);
    }
  }
}
