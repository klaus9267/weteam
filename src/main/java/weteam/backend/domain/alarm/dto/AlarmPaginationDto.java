package weteam.backend.domain.alarm.dto;

import org.springframework.data.domain.Page;
import weteam.backend.domain.alarm.Alarm;

import java.util.List;

public record AlarmPaginationDto(
        int totalPages,
        int totalElements,
        List<AlarmDto> alarmList
) {
    public static AlarmPaginationDto from(Page<Alarm> alarmPage) {
        return new AlarmPaginationDto(alarmPage.getTotalPages(), alarmPage.getNumberOfElements(), AlarmDto.from(alarmPage.getContent()));
    }
}
