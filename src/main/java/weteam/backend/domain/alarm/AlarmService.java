package weteam.backend.domain.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.domain.alarm.dto.AlarmDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectUser;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public Page<AlarmDto> readAlarmList(final Pageable pageable, final Long userId) {
        return alarmRepository.findAll(pageable, userId);
    }

    @Transactional
    public void addAlarm(final Project project, final AlarmStatus status) {
        List<ProjectUser> projectUserList = project.getProjectUserList();
        List<Alarm> alarmList = Alarm.from(project, status);
        alarmRepository.saveAll(alarmList);
    }

    @Transactional
    public void addAlarmWithTargetUser(final Project project, final AlarmStatus status, final Long userId) {
        List<ProjectUser> projectUserList = project.getProjectUserList();
        List<Alarm> alarmList = Alarm.from(project, status);
        alarmRepository.saveAll(alarmList);
    }
}
