package weteam.backend.domain.alarm;

import jakarta.persistence.*;
import lombok.Getter;
import weteam.backend.domain.user.entity.User;

@Entity(name = "alarms")
@Getter
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @Column(columnDefinition = "boolean default false")
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
