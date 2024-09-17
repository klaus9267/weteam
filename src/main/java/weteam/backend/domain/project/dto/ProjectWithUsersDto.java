package weteam.backend.domain.project.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.dto.UserWithProfileImageDto;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProjectWithUsersDto {
  private final Long id;
  private final Long imageId;
  private final String name;
  private final String hashedId;
  private final String explanation;
  private final Long headCount;
  private final boolean done;
  private final UserWithProfileImageDto host;
  private final LocalDate startedAt;
  private final LocalDate endedAt;

  private ProjectWithUsersDto(final Project project) {
    this.id = project.getId();
    this.imageId = project.getImageId();
    this.name = project.getName();
    this.hashedId = project.getHashedId();
    this.explanation = project.getExplanation();
    this.headCount = project.getProjectUserList().stream().filter(projectUser -> !projectUser.isBlack()).count();
    this.done = project.isDone();
    this.host = UserWithProfileImageDto.from(project.getHost());
    this.startedAt = project.getStartedAt();
    this.endedAt = project.getEndedAt();
  }

  public static ProjectWithUsersDto from(final Project project) {
    return new ProjectWithUsersDto(project);
  }

  public static List<ProjectWithUsersDto> from(final List<Project> projectList) {
    return projectList.stream().map(ProjectWithUsersDto::from).toList();
  }
}
