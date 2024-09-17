package weteam.backend.domain.project_user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.domain.alarm.AlarmService;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.project.ProjectService;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project_user.dto.ProjectUserDto;
import weteam.backend.domain.project_user.entity.ProjectUser;
import weteam.backend.domain.project_user.param.UpdateProjectRoleParam;
import weteam.backend.domain.user.UserService;
import weteam.backend.domain.user.entity.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectUserFacade {
  private final ProjectUserService projectUserService;
  private final ProjectService projectService;
  private final UserService userService;
  private final AlarmService alarmService;
  private final SecurityUtil securityUtil;

  public String findProjectHashedId(final long projectId) {
    final Project project = projectService.findProject(projectId);
    return project.getHashedId();
  }

  public List<ProjectUserDto> findProjectUsers(final long projectId) {
    final List<ProjectUser> projectUsers = projectUserService.findProjectUsers(projectId);
    return ProjectUserDto.from(projectUsers);
  }

  @Transactional
  public void updateProjectRole(final UpdateProjectRoleParam updateProjectRoleParam) {
    final long currentUserId = securityUtil.getCurrentUserId();
    projectUserService.updateProjectRole(updateProjectRoleParam, currentUserId);
  }

  @Transactional
  public void acceptInvite(final String hashedProjectId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final User user = userService.findUser(currentUserId);
    final Project project = projectService.findProject(hashedProjectId);

    projectUserService.acceptInvite(project, user);
    alarmService.addAlarmListWithTargetUser(project, AlarmStatus.JOIN, user);
  }

  @Transactional
  public void kickUsers(final List<Long> projectUserIds) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final Project project = projectUserService.kickUsers(projectUserIds, currentUserId);
    final List<User> users = project.getProjectUserList().stream()
        .map(ProjectUser::getUser)
        .toList();
    alarmService.addAlarmListWithTargetUserList(project, AlarmStatus.KICK, users);
  }

  @Transactional
  public void leaveProject(final long projectId) {
    final long currentUserId = securityUtil.getCurrentUserId();
    final User user = userService.findUser(currentUserId);
    final Project project = projectService.findProject(projectId);
    final Project leftProject = projectUserService.leaveProject(project, user);
    alarmService.addAlarmListWithTargetUser(leftProject, AlarmStatus.EXIT, user);
  }
}
