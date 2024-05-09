package weteam.backend.domain.alarm;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "alarms")
@Getter
@NoArgsConstructor
public class Alarm {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private AlarmStatus status;
  private LocalDate date;

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
    this.date = LocalDate.now();
  }

  private Alarm(final Project project, final AlarmStatus status, final User user, final User targetUser) {
    this.status = status;
    this.project = project;
    this.user = user;
    this.date = LocalDate.now();
    this.targetUser = targetUser;
  }

  public static List<Alarm> from(final Project project, final AlarmStatus status) {
    return project.getProjectUserList()
        .stream()
        .filter(projectUser -> projectUser.isEnable() && projectUser.getUser().isLogin())
        .map(projectUser -> new Alarm(project, status, projectUser.getUser()))
        .toList();
  }

  public static List<Alarm> from(final Project project, final AlarmStatus status, final User targetUser) {
    return project.getProjectUserList()
        .stream()
        .filter(projectUser -> {
              if (AlarmStatus.CHANGE_HOST.equals(status)) return projectUser.isEnable() && projectUser.getUser().isLogin();
              return !projectUser.getUser().getId().equals(targetUser.getId())
                  && projectUser.getUser().isLogin()
                  && projectUser.isEnable();
            }
        )
        .map(projectUser -> new Alarm(project, status, projectUser.getUser(), targetUser))
        .toList();
  }

  public void changeIsRead() {
    this.isRead = !isRead;
  }
}
