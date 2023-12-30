package weteam.backend.domain.alarm.dto;

import lombok.Getter;
import weteam.backend.domain.alarm.Alarm;

@Getter
public class AlarmDto {
    private final Long id;
    private final String content;
    private final boolean isRead;

    public AlarmDto(Alarm alarm) {
        this.id = alarm.getId();
        this.content = alarm.getContent();
        this.isRead = alarm.isRead();
    }
}
