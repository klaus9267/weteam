package weteam.backend.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.AlarmService;
import weteam.backend.domain.alarm.entity.AlarmStatus;
import weteam.backend.domain.common.HashUtil;
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

@Service
@RequiredArgsConstructor
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;
  private final AlarmService alarmService;
  private final SecurityUtil securityUtil;

  @Transactional
  public void addProject(final CreateProjectDto projectDto) {
    if (projectRepository.findByHostIdAndNameAndExplanation(securityUtil.getId(), projectDto.name(), projectDto.explanation()).isPresent()) {
      throw new CustomException(CustomErrorCode.DUPLICATE);
    }

    final Project project = Project.from(projectDto, securityUtil.getCurrentUser());
    Project savedProject = projectRepository.save(project);

    final String hashedId = HashUtil.hashId(savedProject.getId());
    savedProject.addHashedId(hashedId);
  }

  private Project findProjectByIdAndUserId(final Long projectId) {
    return projectRepository.findByIdAndUserId(projectId, securityUtil.getId()).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND_PROJECT));
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
//    alarmService.addList(project, AlarmStatus.DONE);
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
    final User newHost = userRepository.findById(newHostId).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND_USER));
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
      throw new CustomException(CustomErrorCode.INVALID_HOST);
    }
    return project;
  }

  @Scheduled(fixedRate = 600000) // 60 * 60 * 1000 밀리초
  @Transactional
  public void checkProject() {
    final LocalDate now = LocalDate.now();
    projectRepository.findAllByIsDoneAndEndedAtBefore(false, now).forEach(Project::updateDone);
  }
}
