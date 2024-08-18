package weteam.backend.domain.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.alarm.AlarmService;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.common.pagination.param.ProjectPaginationParam;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.ProjectDto;
import weteam.backend.domain.project.dto.ProjectPaginationDto;
import weteam.backend.domain.project.dto.UpdateProjectDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.repository.ProjectRepository;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;
  private final AlarmService alarmService;
  private final SecurityUtil securityUtil;

  @Transactional
  public void addProject(final CreateProjectDto projectDto) {
    if (projectDto.startedAt().isAfter(projectDto.endedAt()) || projectDto.endedAt().isBefore(projectDto.startedAt())) {
      throw new CustomException(ErrorCode.INVALID_TIME);
    }

    projectRepository.findByHostIdAndNameAndExplanation(securityUtil.getId(), projectDto.name(), projectDto.explanation())
        .ifPresent(project -> {
          throw new CustomException(ErrorCode.DUPLICATE);
        });

    final Project project = Project.from(projectDto, securityUtil.getCurrentUser());
    projectRepository.save(project);
  }

  private Project findProjectByIdAndUserId(final Long projectId) {
    return projectRepository.findByIdAndUserId(projectId, securityUtil.getId()).orElseThrow(CustomException.raise(ErrorCode.PROJECT_NOT_FOUND));
  }

  public ProjectPaginationDto findListWithPagination(final ProjectPaginationParam paginationParam) {
    final Page<Project> projectPage = projectRepository.findAllByUserIdAndIsDone(paginationParam.toPageable(), paginationParam.getUserId(), paginationParam.isDone());
    return ProjectPaginationDto.from(projectPage);
  }

  public ProjectDto findProjectDto(final Long projectId) {
    final Project project = this.findProjectByIdAndUserId(projectId);
    return ProjectDto.from(project);
  }

  @Transactional
  public void markAsDone(final Long projectId) {
    Project project = this.checkHost(projectId);
    project.updateDone();
    alarmService.addAlarmList(project, AlarmStatus.DONE);
  }

  @Transactional
  public void updateProject(final UpdateProjectDto projectDto, final Long projectId) {
    Project project = this.checkHost(projectId);
    project.updateProject(projectDto);
    alarmService.addAlarmList(project, AlarmStatus.UPDATE_PROJECT);
  }

  @Transactional
  public void updateHost(final Long projectId, final Long newHostId) {
    final User newHost = userRepository.findById(newHostId).orElseThrow(CustomException.raise(ErrorCode.USER_NOT_FOUND));
    Project project = this.checkHost(projectId);
    project.updateHost(newHost);
    alarmService.addAlarmListWithTargetUser(project, AlarmStatus.CHANGE_HOST, newHost);
  }

  @Transactional
  public void deleteProject(final Long projectId) {
    Project project = this.checkHost(projectId);
    projectRepository.delete(project);
  }

  private Project checkHost(final Long projectId) {
    Project project = this.findProjectByIdAndUserId(projectId);
    if (!project.getHost().getId().equals(securityUtil.getId())) {
      throw new CustomException(ErrorCode.INVALID_HOST);
    }
    return project;
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
