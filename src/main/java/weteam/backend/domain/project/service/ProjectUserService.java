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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
  public void acceptInvite(final Long projectId) {
    final Project project = projectRepository.findById(projectId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
    final ProjectUser projectUser = ProjectUser.from(project, securityUtil.getId());
    project.addProjectUser(projectUser);
    alarmService.addListWithTargetUser(projectUser.getProject(), AlarmStatus.JOIN, securityUtil.getId());
  }
  
  @Transactional
  public String createInviteUrl(final Long projectId) {
    try {
      // 숫자를 문자열로 변환하고 MD5 해시 생성
      MessageDigest digest = MessageDigest.getInstance("MD5");
      byte[] encodedHash = digest.digest(Long.toString(projectId).getBytes(StandardCharsets.UTF_8));
      
      // byte 배열을 Hex 문자열로 변환
      StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
      for (byte b : encodedHash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
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
    List<ProjectUser> projectUser = projectUserRepository.findAllById(projectUserIdList).stream().filter(ProjectUser::isEnable).toList();
    if (projectUser.isEmpty()) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, "프로젝트에 참가한 유저가 없습니다.");
    }
    projectUser.forEach(user -> {
      user.disable();
      alarmService.addListWithTargetUser(user.getProject(), AlarmStatus.KICK, user.getId());
    });
  }
  
  @Transactional
  public void exitProject(final Long projectId) {
    ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(projectId, securityUtil.getId()).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND_PROJECT));
    projectUser.disable();
    alarmService.addListWithTargetUser(projectUser.getProject(), AlarmStatus.EXIT, securityUtil.getId());
  }
}
