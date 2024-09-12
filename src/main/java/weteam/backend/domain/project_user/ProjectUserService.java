package weteam.backend.domain.project_user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.alarm.AlarmService;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.project_user.dto.ProjectUserDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project_user.param.UpdateProjectRoleParam;
import weteam.backend.domain.project.ProjectRepository;
import weteam.backend.domain.project_user.entity.ProjectUser;
import weteam.backend.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectUserService {
  private final ProjectUserRepository projectUserRepository;
  private final ProjectRepository projectRepository;
  private final AlarmService alarmService;
  private final SecurityUtil securityUtil;

  public List<ProjectUserDto> findProjectUserListByProjectId(final Long projectId) {
    final List<ProjectUser> projectUserList = projectUserRepository.findAllByProjectId(projectId);
    if (projectUserList.isEmpty()) {
      throw new CustomException(ErrorCode.PROJECT_USER_NOT_FOUND);
    }
    return ProjectUserDto.from(projectUserList);
  }

  @Transactional
  public void acceptInvite(final String hashedProjectId) {
    final Project project = projectRepository.findByHashedId(hashedProjectId).orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
    final Optional<ProjectUser> optionalProjectUser = project.findProjectUserByUser(securityUtil.getCurrentUser());
    final User currentUser = securityUtil.getCurrentUser();

    optionalProjectUser.ifPresentOrElse(projectUser -> {
      if (projectUser.isBlack() || projectUser.isEnable()) {
        throw new CustomException(ErrorCode.PROJECT_ACCESS_DENIED);
      } else {
        projectUser.enable();
        alarmService.addAlarmListWithTargetUser(project, AlarmStatus.JOIN, currentUser);
      }
    }, () -> {
      project.addProjectUser(currentUser);
      alarmService.addAlarmListWithTargetUser(project, AlarmStatus.JOIN, currentUser);
    });
  }

  @Transactional
  public String readHashedId(final Long projectId) {
    return projectRepository.findById(projectId).orElseThrow(CustomException.raise(ErrorCode.PROJECT_NOT_FOUND)).getHashedId();
  }

  @Transactional
  public void updateProjectRole(final UpdateProjectRoleParam param) {
    ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(param.getProjectId(), securityUtil.getCurrentUserId()).orElseThrow(CustomException.raise(ErrorCode.PROJECT_USER_NOT_FOUND));
    projectUser.updateRole(param.getRole());
  }

  @Transactional
  public void kickUsers(final List<Long> projectUserIdList) {
    final Project project = projectRepository.findByProjectUserListIdIn(projectUserIdList).orElseThrow(CustomException.raise(ErrorCode.PROJECT_NOT_FOUND));
    if (!project.getHost().getId().equals(securityUtil.getCurrentUserId())) {
      throw new CustomException(ErrorCode.INVALID_HOST);
    }

    final List<User> userList = new ArrayList<>();

    for (final ProjectUser projectUser : project.getProjectUserList()) {
      if (projectUserIdList.contains(projectUser.getId())) {
        userList.add(projectUser.getUser());
        projectUser.kick();
      }
    }

    alarmService.addAlarmListWithTargetUserList(project, AlarmStatus.KICK, userList);
  }

  @Transactional
  public void exitProject(final Long projectId) {
    final ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(projectId, securityUtil.getCurrentUserId()).orElseThrow(CustomException.raise(ErrorCode.PROJECT_NOT_FOUND));
    if (projectUser.getProject().getHost().getId().equals(securityUtil.getCurrentUserId())) {
      throw new CustomException(ErrorCode.USER_IS_HOST);
    }
    projectUser.disable();
    alarmService.addAlarmListWithTargetUser(projectUser.getProject(), AlarmStatus.EXIT, securityUtil.getCurrentUser());
  }
}
