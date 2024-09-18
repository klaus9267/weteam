package weteam.backend.domain.project_user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.project.ProjectService;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project_user.entity.ProjectUser;
import weteam.backend.domain.project_user.param.UpdateProjectRoleParam;
import weteam.backend.domain.user.entity.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectUserService {
  private final ProjectUserRepository projectUserRepository;

  public List<ProjectUser> findProjectUsers(final Long projectId) {
    final List<ProjectUser> projectUserList = projectUserRepository.findAllByProjectId(projectId);
    if (projectUserList == null || projectUserList.isEmpty()) {
      throw new CustomException(ErrorCode.PROJECT_USER_NOT_FOUND);
    }
    return projectUserList;
  }

  public ProjectUser findProjectUser(final long projectId, final long userId) {
    return projectUserRepository.findByProjectIdAndUserId(projectId, userId).orElseThrow(CustomException.raise(ErrorCode.PROJECT_USER_NOT_FOUND));
  }

  @Transactional
  public void acceptInvite(final Project project, final User user) {
    projectUserRepository.findByProjectIdAndUserId(project.getId(), user.getId())
        .ifPresentOrElse(projectUser -> {
          throw new CustomException(ErrorCode.PROJECT_ACCESS_DENIED);
        }, () -> {
          final ProjectUser newProjectUser = ProjectUser.from(project, user);
          projectUserRepository.save(newProjectUser);
        });
  }

  @Transactional
  public void updateProjectRole(final UpdateProjectRoleParam updateProjectRoleParam, final long userId) {
    final ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(updateProjectRoleParam.getProjectId(), userId).orElseThrow(CustomException.raise(ErrorCode.PROJECT_USER_NOT_FOUND));
    projectUser.updateRole(updateProjectRoleParam.getRole());
  }

  @Transactional
  public Project kickUsers(final List<Long> projectUserIdList, final long userId) {
    if (projectUserIdList == null || projectUserIdList.isEmpty()) {
      throw new CustomException(ErrorCode.INVALID_LIST);
    }
    final List<ProjectUser> projectUsers = projectUserRepository.findAllByIdInWithSameProject(projectUserIdList, projectUserIdList.size());
    final Project project = projectUsers.get(0).getProject();
    ProjectService.checkHost(project, userId);
    projectUsers.forEach(ProjectUser::kick);
    projectUserRepository.saveAll(projectUsers);
    return project;
  }

  @Transactional
  public Project leaveProject(final Project project, final User user) {
    if (project.getHost().getId().equals(user.getId())) {
      throw new CustomException(ErrorCode.USER_IS_HOST);
    }
    final ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(project.getId(), user.getId()).orElseThrow(CustomException.raise(ErrorCode.PROJECT_NOT_FOUND));
    project.leaveProject(projectUser);
    return project;
  }
}
