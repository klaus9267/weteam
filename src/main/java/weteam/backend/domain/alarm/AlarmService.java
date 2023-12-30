package weteam.backend.domain.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import weteam.backend.domain.alarm.dto.AlarmDto;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public Page<AlarmDto> readAlarmList(final Pageable pageable, final Long userId) {
        return alarmRepository.findAll(pageable, userId);
    }
}
