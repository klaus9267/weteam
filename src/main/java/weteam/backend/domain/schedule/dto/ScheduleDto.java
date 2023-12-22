package weteam.backend.domain.schedule.dto;

import lombok.Builder;
import weteam.backend.domain.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record ScheduleDto(
        Long id,
        String title,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        String place,
        LocalDateTime alarm,
        int repeatType,
        String memo,
        String color) {

    public static ScheduleDto from(Schedule schedule) {
        return ScheduleDto.builder()
                          .id(schedule.getId())
                          .title(schedule.getTitle())
                          .startedAt(schedule.getStartedAt())
                          .endedAt(schedule.getEndedAt())
                          .place(schedule.getPlace())
                          .alarm(schedule.getAlarm())
                          .repeatType(schedule.getRepeatType())
                          .memo(schedule.getMemo())
                          .color(schedule.getColor())
                          .build();
    }

    public static List<ScheduleDto> from(List<Schedule> scheduleList) {
        return scheduleList.stream()
                           .map(ScheduleDto::from)
                           .collect(Collectors.toList());
    }
}
