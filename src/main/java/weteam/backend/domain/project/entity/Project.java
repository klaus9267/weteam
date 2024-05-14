package weteam.backend.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.application.BaseEntity;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.Alarm;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.UpdateProjectDto;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
  private Integer imageId;
  private String name, explanation, hashedId;
  private LocalDate startedAt, endedAt;

  @Column(columnDefinition = "boolean default false")
  private boolean done;

  @ManyToOne(fetch = FetchType.LAZY)
  private User host;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private List<ProjectUser> projectUserList = new ArrayList<>();

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private List<Alarm> alarmList = new ArrayList<>();

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private List<Meeting> meetingList = new ArrayList<>();

  public Project(CreateProjectDto projectDto, User user) {
    ProjectUser projectUser = ProjectUser.from(user, this);

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
    this.done = !this.isDone();
  }

  public void updateHost(final User newHost) {
    if (this.host.equals(newHost)) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, "이미 호스트로 진행중인 프로젝트입니다.");
    }
    this.host = newHost;
  }

  public void updateProject(final UpdateProjectDto projectDto) {
    this.name = projectDto.name() == null ? name : projectDto.name();
    this.startedAt = projectDto.startedAt();
    this.explanation = projectDto.explanation() == null ? explanation : projectDto.explanation();
    this.endedAt = projectDto.endedAt();
    if (this.done) {
      this.done = false;
    }
  }

  public void addProjectUser(final ProjectUser newProjectUser) {
    final Long currentUserId = newProjectUser.getUser().getId();

    for (ProjectUser projectUser : this.projectUserList) {
      if (projectUser.getUser().getId().equals(currentUserId)) {
        throw new CustomException(CustomErrorCode.BAD_REQUEST, "이미 수락한 팀플입니다.");
      }
    }
    this.getProjectUserList().add(newProjectUser);
  }

  public void addHashedId(final String hashedId) {
    this.hashedId = hashedId;
  }

  public void kickUsers(final List<ProjectUser> projectUsers) {
    this.projectUserList.removeAll(projectUsers);
  }
}
