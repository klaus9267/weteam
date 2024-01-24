package weteam.backend.domain.alarm;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.entity.User;

import java.util.List;

@Entity(name = "alarms")
@Getter
@NoArgsConstructor
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private AlarmStatus status;

    @Column(columnDefinition = "boolean default false")
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private User targetUser;

    private Alarm(final Project project, final AlarmStatus status, final User user) {
        this.status = status;
        this.project = project;
        this.user = user;
    }

    public static List<Alarm> from(final Project project, final AlarmStatus status) {
        return project.getProjectUserList().stream().map(projectUser -> new Alarm(project, status, projectUser.getUser())).toList();
    }

    public static List<Alarm> from(final Project project, final AlarmStatus status, final Long userId) {
        return project.getProjectUserList()
                      .stream()
                      .filter(projectUser -> !projectUser.getUser().getId().equals(userId))
                      .map(projectUser -> new Alarm(project, status, projectUser.getUser()))
                      .toList();
    }

    public void changeIsRead() {
        this.isRead = !isRead;
    }
}
