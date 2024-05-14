package weteam.backend.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.AlarmService;
import weteam.backend.domain.alarm.AlarmStatus;
import weteam.backend.domain.project.dto.ProjectUserDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.project.param.UpdateProjectRoleParam;
import weteam.backend.domain.project.repository.ProjectRepository;
import weteam.backend.domain.project.repository.ProjectUserRepository;
import weteam.backend.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectUserService {
  private final ProjectUserRepository projectUserRepository;
  private final ProjectRepository projectRepository;
  private final AlarmService alarmService;
  private final SecurityUtil securityUtil;

  @Transactional
  public List<ProjectUserDto> findProjectUserListByProjectId(final Long projectId) {
    final List<ProjectUser> projectUserList = projectUserRepository.findByProjectId(projectId);
    if (projectUserList.isEmpty()) {
      throw new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER);
    }
    return ProjectUserDto.from(projectUserList);
  }

  @Transactional
  public void acceptInvite4Develop(final Long projectId) {
    final Project project = projectRepository.findById(projectId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
    final ProjectUser projectUser = ProjectUser.from(project, securityUtil.getId());
    project.addProjectUser(projectUser);
    alarmService.addListWithTargetUser(projectUser.getProject(), AlarmStatus.JOIN, securityUtil.getCurrentUser());
  }

  @Transactional
  public void acceptInvite(final String hashedProjectId) {
    final Project project = projectRepository.findByHashedId(hashedProjectId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
    final ProjectUser projectUser = ProjectUser.from(project, securityUtil.getId());
    project.addProjectUser(projectUser);
    alarmService.addListWithTargetUser(projectUser.getProject(), AlarmStatus.JOIN, securityUtil.getCurrentUser());
  }

  @Transactional
  public String readHashedId(final Long projectId) {
    final Project project = projectRepository.findById(projectId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
    return project.getHashedId();
  }

  @Transactional
  public void updateProjectRole(final UpdateProjectRoleParam param) {
    ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(param.getProjectId(), securityUtil.getId()).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND_PROJECT_USER));
    projectUser.updateRole(param.getRole());
  }

  @Transactional
  public void kickUsers(final List<Long> projectUserIdList) {
    final Project project = projectRepository.findByProjectUserListIdIn(projectUserIdList).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
    if (!project.getHost().getId().equals(securityUtil.getId())) {
      throw new CustomException(CustomErrorCode.INVALID_HOST);
    }
    projectUserRepository.deleteAllById(projectUserIdList);

    final List<User> userList = new ArrayList<>();
    for (final ProjectUser projectUser : project.getProjectUserList()) {
      final User user = projectUser.getUser();
      if (projectUserIdList.contains(user.getId())) userList.add(user);
    }
    alarmService.addListWithTargetUserList(project, AlarmStatus.KICK, userList);
  }

  @Transactional
  public void exitProject(final Long projectId) {
    ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(projectId, securityUtil.getId()).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND_PROJECT));
    if (projectUser.getProject().getHost().getId().equals(securityUtil.getId())) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, "호스트를 넘기기전에 탈퇴할 수 없습니다.");
    }
    projectUser.disable();
    alarmService.addListWithTargetUser(projectUser.getProject(), AlarmStatus.EXIT, securityUtil.getCurrentUser());
  }
}
