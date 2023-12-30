package weteam.backend.domain.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DoneState {
    DONE("종료"),
    PROGRESS("진행중");

    private final String state;
}
