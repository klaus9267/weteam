package weteam.backend.domain.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
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
  
  @Column(columnDefinition = "boolean default true")
  private boolean enable;
  
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;
  
  @ManyToOne(fetch = FetchType.LAZY)
  private Project project;
  
  public static ProjectUser from(final User user, final Project project) {
    return ProjectUser.builder().user(user).project(project).enable(true).build();
  }
  
  public static ProjectUser from(final Project project, final Long userId) {
    return ProjectUser.builder().project(project).user(User.from(userId)).enable(true).build();
  }
  
  public void updateRole(final String role) {
    this.role = role;
  }
  
  public void disable() {
    if (!this.enable) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, "이미 탈퇴한 프로젝트입니다.");
    }
    this.enable = false;
  }
}
