package weteam.backend.domain.project_user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import weteam.backend.domain.project_user.entity.ProjectUser;
import weteam.backend.domain.user.dto.UserWithProfileImageDto;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
public class ProjectUserDto {
  private Long id;
  private String role;
  private boolean enable;
  private UserWithProfileImageDto user;

  private ProjectUserDto(ProjectUser projectUser) {
    this.id = projectUser.getId();
    this.role = projectUser.getRole();
    this.user = UserWithProfileImageDto.from(projectUser.getUser());
    this.enable = projectUser.isEnable();
  }

  public static ProjectUserDto from(ProjectUser projectUser) {
    return new ProjectUserDto(projectUser);
  }

  public static List<ProjectUserDto> from(List<ProjectUser> projectUserList) {
    return projectUserList.stream().map(ProjectUserDto::new).collect(Collectors.toList());
  }
}
