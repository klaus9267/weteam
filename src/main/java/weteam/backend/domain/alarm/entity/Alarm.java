package weteam.backend.domain.alarm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "alarms")
@Getter
@NoArgsConstructor
public class Alarm {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private AlarmStatus status;
  private LocalDateTime date;

  @Column(columnDefinition = "boolean default false")
  private boolean isRead;

  @ManyToOne(fetch = FetchType.LAZY)
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY)
  private Meeting meeting;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private User targetUser;

  private Alarm(final Project project, final AlarmStatus status, final User user) {
    this.status = status;
    this.project = project;
    this.user = user;
    this.date = LocalDateTime.now();
  }

  private Alarm(final Project project, final AlarmStatus status, final User user, final User targetUser) {
    this.status = status;
    this.project = project;
    this.user = user;
    this.date = LocalDateTime.now();
    this.targetUser = targetUser;
  }

  private Alarm(final Meeting meeting, final AlarmStatus status, final User user) {
    this.status = status;
    this.meeting = meeting;
    this.user = user;
    this.date = LocalDateTime.now();
  }

  private Alarm(final Meeting meeting, final AlarmStatus status, final User user, final User targetUser) {
    this.status = status;
    this.meeting = meeting;
    this.user = user;
    this.date = LocalDateTime.now();
    this.targetUser = targetUser;
  }

  public static List<Alarm> from(final Project project, final AlarmStatus status, final User targetUser) {
    return project.getProjectUserList()
        .stream()
        .filter(projectUser -> {
              final User user = projectUser.getUser();
              return user.isLogin() && user.isReceivePermission() && projectUser.isEnable() && !user.getId().equals(targetUser.getId());
            }
        ).map(projectUser -> new Alarm(project, status, projectUser.getUser(), targetUser))
        .toList();
  }

  public static List<Alarm> from(final Project project, final AlarmStatus status) {
    return project.getProjectUserList()
        .stream()
        .filter(projectUser -> {
              final User user = projectUser.getUser();
              return user.isLogin() && user.isReceivePermission() && projectUser.isEnable();
            }
        ).map(projectUser -> new Alarm(project, status, projectUser.getUser()))
        .toList();
  }

  public static List<Alarm> from(final Meeting meeting, final AlarmStatus status) {
    return meeting.getMeetingUserList()
        .stream()
        .filter(meetingUser -> {
              final User user = meetingUser.getUser();
              return user.isLogin() && user.isReceivePermission() && meetingUser.isDisplayed();
            }
        ).map(meetingUser -> new Alarm(meeting, status, meetingUser.getUser()))
        .toList();
  }

  public static List<Alarm> from(final Meeting meeting, final AlarmStatus status, final User targetUser) {
    return meeting.getMeetingUserList()
        .stream()
        .filter(meetingUser -> {
              final User user = meetingUser.getUser();
              return user.isLogin() && user.isReceivePermission() && meetingUser.isDisplayed() && !user.getId().equals(targetUser.getId());
            }
        ).map(meetingUser -> new Alarm(meeting, status, meetingUser.getUser()))
        .toList();
  }

  public void markAsRead() {
    this.isRead = true;
  }
}
