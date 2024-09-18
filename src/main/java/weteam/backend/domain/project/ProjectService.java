package weteam.backend.domain.project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.common.pagination.param.ProjectPaginationParam;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.UpdateProjectDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
  private final ProjectRepository projectRepository;

  public void addProject(final CreateProjectDto createProjectDto, final User user) {
    if (createProjectDto.startedAt().isAfter(createProjectDto.endedAt()) || createProjectDto.endedAt().isBefore(createProjectDto.startedAt())) {
      throw new CustomException(ErrorCode.INVALID_TIME);
    }

    projectRepository.findByHostIdAndNameAndExplanation(user.getId(), createProjectDto.name(), createProjectDto.explanation())
        .ifPresent(project -> {
          throw new CustomException(ErrorCode.DUPLICATE);
        });

    final Project project = Project.from(createProjectDto, user);
    projectRepository.save(project);
  }

  public Project findProjectByIdAndUserId(final long projectId, final long userId) {
    return projectRepository.findByIdAndUserId(projectId, userId).orElseThrow(CustomException.raise(ErrorCode.PROJECT_NOT_FOUND));
  }

  public Project findProject(final long projectId) {
    return projectRepository.findById(projectId).orElseThrow(CustomException.raise(ErrorCode.PROJECT_NOT_FOUND));
  }

  public Project findProject(final String hashedProjectId) {
    return projectRepository.findByHashedId(hashedProjectId).orElseThrow(CustomException.raise(ErrorCode.PROJECT_NOT_FOUND));
  }

  public Optional<Project> findOptionalProject(final long projectId) {
    return projectRepository.findById(projectId);
  }

  public Page<Project> pagingProjects(final ProjectPaginationParam paginationParam) {
    return projectRepository.findAllByUserIdAndIsDone(paginationParam.toPageable(), paginationParam.getUserId(), paginationParam.isDone());
  }

  public Project toggleIsDone(final long projectId, final long userId) {
    final Project project = this.findProjectByIdAndUserId(projectId, userId);
    checkHost(project, userId);
    project.updateDone();
    return project;
  }

  public Project updateProject(final UpdateProjectDto projectDto, final Long projectId, final long userId) {
    final Project project = this.findProjectByIdAndUserId(projectId, userId);
    checkHost(project, userId);
    project.updateProject(projectDto);
    return project;
  }

  public Project updateHost(final Long projectId, final long userId, final User newHost) {
    final Project project = this.findProjectByIdAndUserId(projectId, userId);
    checkHost(project, userId);
    project.updateHost(newHost);
    return project;
  }

  public void deleteProject(final Long projectId, final long userId) {
    final Project project = this.findProjectByIdAndUserId(projectId, userId);
    checkHost(project, userId);
    projectRepository.delete(project);
  }

  public static void checkHost(final Project project, final long userId) {
    if (!project.getHost().getId().equals(userId)) {
      throw new CustomException(ErrorCode.INVALID_HOST);
    }
  }

  @Scheduled(fixedRate = 1000 * 60 * 60) // 1시간
  @Transactional
  public void checkProject() {
    final LocalDate now = LocalDate.now();
    log.info("Project check time : " + LocalDateTime.now());
    projectRepository.findAllByIsDoneAndEndedAtBefore(false, now).forEach(Project::updateDone);
    log.info("-------------------------------------------------------------------------------------------------------------------");
  }
}
