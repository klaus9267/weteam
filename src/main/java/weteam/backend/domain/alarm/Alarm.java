package weteam.backend.domain.alarm;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.domain.common.enums.AlarmState;
import weteam.backend.domain.user.entity.User;

@Entity(name = "alarms")
@Getter
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @Enumerated(EnumType.STRING)
    private AlarmState state;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
