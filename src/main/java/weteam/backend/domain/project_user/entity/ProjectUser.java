package weteam.backend.domain.project_user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.entity.User;

@Entity(name = "project_users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ProjectUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String role;

  @Builder.Default
  private boolean isBlack = false;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Project project;

  public static ProjectUser from(final Project project, final User user) {
    return ProjectUser.builder()
        .project(project)
        .user(user)
        .build();
  }

  public void updateRole(final String role) {
    this.role = role;
  }
  public void kick() {
    this.isBlack = true;
  }
}
