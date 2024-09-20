package weteam.backend.domain.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.domain.alarm.dto.AlarmPaginationDto;
import weteam.backend.domain.alarm.entity.Alarm;
import weteam.backend.domain.common.pagination.param.AlarmPaginationParam;

@Component
@RequiredArgsConstructor
public class AlarmFacade {
  private final AlarmService alarmService;
  private final SecurityUtil securityUtil;

  @Transactional(readOnly = true)
  public AlarmPaginationDto pagingAlarms(final AlarmPaginationParam alarmPaginationParam) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Page<Alarm> alarmPage = alarmService.readAlarmList(alarmPaginationParam, currentUserId);
    return AlarmPaginationDto.from(alarmPage);
  }

  @Transactional
  public void updateAlarmRead(final long alarmId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    alarmService.updateAlarmRead(alarmId, currentUserId);
  }

  @Transactional
  public void updateAllRead() {
    final long currentUserId = securityUtil.getCurrentUserId();
    alarmService.updateAllRead(currentUserId);
  }
}
