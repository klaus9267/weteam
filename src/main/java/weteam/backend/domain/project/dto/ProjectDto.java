package weteam.backend.domain.project.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.user.dto.UserDto;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProjectDto {
  private final Long id;
  private final String name;
  private final String hashedId;
  private final String explanation;
  private final Long headCount;
  private final boolean done;
  private final UserDto host;
  private final LocalDate startedAt;
  private final LocalDate endedAt;
  
  private ProjectDto(final Project project) {
    this.id = project.getId();
    this.name = project.getName();
    this.hashedId = project.getHashedId();
    this.explanation = project.getExplanation();
    this.headCount = project.getProjectUserList().stream().filter(ProjectUser::isEnable).count();
    this.done = project.isDone();
    this.host = UserDto.from(project.getHost());
    this.startedAt = project.getStartedAt();
    this.endedAt = project.getEndedAt();
  }
  
  public static ProjectDto from(final Project project) {
    return new ProjectDto(project);
  }
  
  public static List<ProjectDto> from(final List<Project> projectList) {
    return projectList.stream().map(ProjectDto::from).toList();
  }
}
