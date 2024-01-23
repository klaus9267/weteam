package weteam.backend.domain.alarm;

import lombok.Getter;

@Getter
public enum AlarmStatus {
    JOIN, EXIT, CHANGE_HOST, UPDATE_PROJECT, DROP
}
