package weteam.backend.domain.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.dto.AlarmDto;
import weteam.backend.domain.alarm.dto.AlarmPaginationDto;
import weteam.backend.domain.common.pagination.param.AlarmPaginationParam;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.project.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final ProjectRepository projectRepository;
    private final SecurityUtil securityUtil;

    public AlarmPaginationDto readAlarmList(final AlarmPaginationParam paginationParam) {
        Page<AlarmDto> alarmPage = alarmRepository.findAll(paginationParam.toPageable(), securityUtil.getId());
        return AlarmPaginationDto.from(alarmPage);
    }

    @Transactional
    public void addAlarm(final Project project, final AlarmStatus status) {
        final List<Alarm> alarmList = Alarm.from(project, status);
        alarmRepository.saveAll(alarmList);
    }

    @Transactional
    public void addAlarmWithTargetUser(final Project project, final AlarmStatus status, final Long userId) {
        final List<Alarm> alarmList = Alarm.from(project, status, userId);
        alarmRepository.saveAll(alarmList);
    }

    @Transactional
    public void addAlarmList(final List<ProjectUser> projectUserList, final AlarmStatus status) {
        List<Alarm> alarmList = new ArrayList<>();
        projectUserList.forEach(projectUser -> alarmList.addAll(Alarm.from(projectUser.getProject(), status, projectUser.getUser().getId())));
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
    public void makeAllAlarmAsRead(final Long userId) {
        List<Alarm> alarmList = alarmRepository.findAllByUserId(userId).stream().filter(alarm -> !alarm.isRead()).toList();
        if (!alarmList.isEmpty()) {
            alarmList.forEach(Alarm::changeIsRead);
        }
    }
}
