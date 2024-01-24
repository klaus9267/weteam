package weteam.backend.domain.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.dto.AlarmDto;
import weteam.backend.domain.alarm.dto.AlarmPaginationDto;
import weteam.backend.domain.project.entity.Project;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public AlarmPaginationDto readAlarmList(final Pageable pageable, final Long userId) {
        Page<AlarmDto> alarmPage = alarmRepository.findAll(pageable, userId);
        return AlarmPaginationDto.from(alarmPage);
    }

    @Transactional
    public void addAlarm(final Project project, final AlarmStatus status) {
        final List<Alarm> alarmList = Alarm.from(project, status);
        alarmRepository.saveAll(alarmList);
    }

    // 다른 서비스에서 사용하는 알람 추가 메서드
    @Transactional
    public void addAlarmWithTargetUser(final Project project, final AlarmStatus status, final Long userId) {
        final List<Alarm> alarmList = Alarm.from(project, status, userId);
        alarmRepository.saveAll(alarmList);
    }

    @Transactional
    public void addAlarmWithTargetUser(final Long projectId, final AlarmStatus status, final Long userId) {
        Project project = Project.builder().id(projectId).build();
        final List<Alarm> alarmList = Alarm.from(project, status, userId);
        alarmRepository.saveAll(alarmList);
    }

    @Transactional
    public void makeAlarmAsRead(final Long alarmId, final Long userId) {
        Alarm alarm = alarmRepository.findByIdAndUserId(alarmId, userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
        if (alarm.isRead()) {
            throw new CustomException(CustomErrorCode.DUPLICATE, "읽은 알람입니다.");
        }
        alarm.changeIsRead();
    }

    @Transactional
    public void makeAllAlarmAsRead( final Long userId) {
        List<Alarm> alarmList = alarmRepository.findAllByUserId(userId).stream().filter(alarm -> !alarm.isRead()).toList();
        if (!alarmList.isEmpty()) {
            alarmList.forEach(Alarm::changeIsRead);
        }
    }
}
