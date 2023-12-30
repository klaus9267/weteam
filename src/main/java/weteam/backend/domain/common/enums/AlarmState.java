package weteam.backend.domain.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmState {
    READ("읽음"),
    UNREAD("읽지 않음");

    private final String state;
}
