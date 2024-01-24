package weteam.backend.domain.alarm.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record AlarmPaginationDto(
        int totalPages,
        int totalElements,
        List<AlarmDto> alarmList
) {
    public static AlarmPaginationDto from(Page<AlarmDto> alarmPage) {
        return new AlarmPaginationDto(alarmPage.getTotalPages(), alarmPage.getNumberOfElements(), alarmPage.getContent());
    }
}
