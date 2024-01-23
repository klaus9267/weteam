package weteam.backend.domain.alarm.dto;

import weteam.backend.domain.alarm.AlarmStatus;

import java.util.List;

public record CreateAlarmDto(
        AlarmStatus status,
        Long projectId,
        List<Long> userIds,
        Long targetUserId
) {
}
