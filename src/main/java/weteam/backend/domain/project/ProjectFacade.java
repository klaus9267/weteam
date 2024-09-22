package weteam.backend.domain.project;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.domain.alarm.AlarmService;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.common.pagination.param.ProjectPaginationParam;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.ProjectDto;
import weteam.backend.domain.project.dto.ProjectPaginationDto;
import weteam.backend.domain.project.dto.UpdateProjectDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.UserService;
import weteam.backend.domain.user.entity.User;

@Component
@RequiredArgsConstructor
public class ProjectFacade {
  private final ProjectService projectService;
  private final UserService userService;
  private final AlarmService alarmService;
  private final SecurityUtil securityUtil;

  @Transactional
  public void addProject(final CreateProjectDto createProjectDto) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final User cuerrentUser = userService.findUser(currentUserId);
    projectService.addProject(createProjectDto, cuerrentUser);
  }

  public ProjectPaginationDto pagingProjects(final ProjectPaginationParam projectPaginationParam) {
    final Page<Project> projectPage = projectService.pagingProjects(projectPaginationParam);
    return ProjectPaginationDto.from(projectPage);
  }

  public ProjectDto findProjectInfo(final long projectId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Project project = projectService.findProjectByIdAndUserId(projectId, currentUserId);
    return ProjectDto.from(project);
  }

  public void toggleIsDone(final long projectId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Project project = projectService.toggleIsDone(projectId, currentUserId);
    alarmService.addAlarms(project, AlarmStatus.DONE);
  }

  public void updateProject(final UpdateProjectDto updateProjectDto, final long projectId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Project project = projectService.updateProject(updateProjectDto, projectId, currentUserId);
    alarmService.addAlarms(project, AlarmStatus.UPDATE_PROJECT);
  }

  public void updateHost(final long projectId, final long useId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final User newHost = userService.findUser(useId);
    final Project project = projectService.updateHost(projectId, currentUserId, newHost);
    alarmService.addAlarms(project, AlarmStatus.CHANGE_HOST, newHost);
  }

  public void deleteProject(final long projectId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    projectService.deleteProject(projectId, currentUserId);
  }
}