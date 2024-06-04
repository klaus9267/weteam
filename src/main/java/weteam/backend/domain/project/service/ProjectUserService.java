package weteam.backend.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.alarm.AlarmService;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.project.dto.ProjectUserDto;
import weteam.backend.domain.project.entity.BlackList;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.project.param.UpdateProjectRoleParam;
import weteam.backend.domain.project.repository.BlackListRepository;
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
  private final BlackListRepository blackListRepository;
  private final AlarmService alarmService;
  private final SecurityUtil securityUtil;

  public List<ProjectUserDto> findProjectUserListByProjectId(final Long projectId) {
    final List<ProjectUser> projectUserList = projectUserRepository.findByProjectId(projectId);
    if (projectUserList.isEmpty()) {
      throw new CustomException(ErrorCode.PROJECT_USER_NOT_FOUND);
    }
    return ProjectUserDto.from(projectUserList);
  }

  @Transactional
  public void acceptInvite(final String hashedProjectId) {
    final Project project = projectRepository.findByHashedId(hashedProjectId).orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
    if (blackListRepository.existsByUserIdAndProjectId(securityUtil.getId(), project.getId())) {
      throw new CustomException(ErrorCode.PROJECT_ACCESS_DENIED);
    }
    project.addProjectUser(securityUtil.getCurrentUser());
    alarmService.addAlarmListWithTargetUser(project, AlarmStatus.JOIN, securityUtil.getCurrentUser());
  }

  @Transactional
  public String readHashedId(final Long projectId) {
    final Project project = projectRepository.findById(projectId).orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
    return project.getHashedId();
  }

  @Transactional
  public void updateProjectRole(final UpdateProjectRoleParam param) {
    ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(param.getProjectId(), securityUtil.getId()).orElseThrow(CustomException.raise(ErrorCode.PROJECT_USER_NOT_FOUND));
    projectUser.updateRole(param.getRole());
  }

  @Transactional
  public void kickUsers(final List<Long> projectUserIdList) {
    final Project project = projectRepository.findByProjectUserListIdIn(projectUserIdList).orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
    if (!project.getHost().getId().equals(securityUtil.getId())) throw new CustomException(ErrorCode.INVALID_HOST);

    final List<BlackList> blackLists = new ArrayList<>();
    final List<User> userList = new ArrayList<>();

    for (final ProjectUser projectUser : project.getProjectUserList()) {
      if (projectUserIdList.contains(projectUser.getId())) {
        blackLists.add(BlackList.from(projectUser));
        userList.add(projectUser.getUser());
      }
    }

    projectUserRepository.deleteAllById(projectUserIdList);
    blackListRepository.saveAll(blackLists);

    alarmService.addAlarmListWithTargetUserList(project, AlarmStatus.KICK, userList);
  }

  @Transactional
  public void exitProject(final Long projectId) {
    ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(projectId, securityUtil.getId()).orElseThrow(CustomException.raise(ErrorCode.PROJECT_NOT_FOUND));
    if (projectUser.getProject().getHost().getId().equals(securityUtil.getId())) {
      throw new CustomException(ErrorCode.USER_IS_HOST);
    }
    projectUser.disable();
    alarmService.addAlarmListWithTargetUser(projectUser.getProject(), AlarmStatus.EXIT, securityUtil.getCurrentUser());
  }
}
