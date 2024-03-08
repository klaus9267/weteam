package weteam.backend.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectUserService {
  private final ProjectUserRepository projectUserRepository;
  private final ProjectRepository projectRepository;
  private final AlarmService alarmService;
  private final SecurityUtil securityUtil;
  
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
    alarmService.addListWithTargetUser(projectUser.getProject(), AlarmStatus.JOIN, securityUtil.getId());
  }
  
  @Transactional
  public void acceptInvite(final String hashedProjectId) {
    final Project project = projectRepository.findByHashedId(hashedProjectId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
    final ProjectUser projectUser = ProjectUser.from(project, securityUtil.getId());
    project.addProjectUser(projectUser);
    alarmService.addListWithTargetUser(projectUser.getProject(), AlarmStatus.JOIN, securityUtil.getId());
  }
  
  @Transactional
  public String createInviteUrl(final Long projectId) {
    try {
      final String hostAddress = InetAddress.getLocalHost().getHostAddress();
      final Project project = projectRepository.findById(projectId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
      return UriComponentsBuilder.fromPath("/").scheme("http").host(hostAddress).port(8080).path("/api/project-users/" + project.getHashedId()).toUriString();
    } catch (UnknownHostException e) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, e.getMessage());
    }
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
    final List<ProjectUser> projectUserList = project.getProjectUserList().stream().filter(projectUser -> projectUserIdList.contains(projectUser.getId())).toList();
    projectUserList.forEach(ProjectUser::disable);
    projectUserList.forEach(projectUser -> alarmService.addListWithTargetUser(project, AlarmStatus.KICK, projectUser.getUser().getId()));
  }
  
  @Transactional
  public void exitProject(final Long projectId) {
    ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(projectId, securityUtil.getId()).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND_PROJECT));
    if (projectUser.getProject().getHost().getId().equals(securityUtil.getId())) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, "호스트를 넘기기전에 탈퇴할 수 없습니다.");
    }
    projectUser.disable();
    alarmService.addListWithTargetUser(projectUser.getProject(), AlarmStatus.EXIT, securityUtil.getId());
  }
  
}
