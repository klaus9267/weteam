package weteam.backend.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.application.BaseEntity;
import weteam.backend.domain.alarm.entity.Alarm;
import weteam.backend.domain.common.HashUtil;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.UpdateProjectDto;
import weteam.backend.domain.project_user.entity.ProjectUser;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode(callSuper = true)
public class Project extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long imageId;
  private String name, explanation, hashedId;
  private LocalDate startedAt, endedAt;

  @Builder.Default
  private boolean isDone = false;

  @ManyToOne(fetch = FetchType.LAZY)
  private User host;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<ProjectUser> projectUserList = new ArrayList<>();

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private final List<Alarm> alarmList = new ArrayList<>();

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private final List<Meeting> meetingList = new ArrayList<>();

  @PostPersist
  public void setHashedId() {
    if (this.hashedId == null || this.hashedId.isBlank()) {
      this.hashedId = HashUtil.hashId(this.id);
    }
  }

  public Project(CreateProjectDto projectDto, User user) {
    ProjectUser projectUser = ProjectUser.from(this, user);
    this.name = projectDto.name();
    this.imageId = projectDto.imageId();
    this.explanation = projectDto.explanation();
    this.startedAt = projectDto.startedAt();
    this.endedAt = projectDto.endedAt();
    this.host = user;
    this.projectUserList.add(projectUser);
  }

  public Project(final Long projectId) {
    this.id = projectId;
  }

  public static Project from(final Long projectId) {
    return new Project(projectId);
  }

  public static Project from(CreateProjectDto projectDto, User user) {
    return new Project(projectDto, user);
  }

  public void updateDone() {
    this.isDone = !this.isDone();
  }

  public void updateHost(final User newHost) {
    this.host = newHost;
  }

  public void updateProject(final UpdateProjectDto projectDto) {
    this.name = projectDto.name() == null ? this.name : projectDto.name();
    this.startedAt = projectDto.startedAt() == null ? this.startedAt : projectDto.startedAt();
    this.explanation = projectDto.explanation() == null ? this.explanation : projectDto.explanation();
    this.endedAt = projectDto.endedAt() == null ? this.endedAt : projectDto.endedAt();
  }

  public void addHashedId(final String hashedId) {
    this.hashedId = hashedId;
  }

  public void leaveProject(final ProjectUser projectUser) {
    this.projectUserList.remove(projectUser);
  }
}
