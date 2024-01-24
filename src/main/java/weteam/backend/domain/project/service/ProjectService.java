package weteam.backend.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.AlarmService;
import weteam.backend.domain.alarm.AlarmStatus;
import weteam.backend.domain.common.pagination.param.ProjectPaginationParam;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.ProjectPaginationDto;
import weteam.backend.domain.project.dto.UpdateProjectDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.repository.ProjectRepository;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final AlarmService alarmService;

    @Transactional
    public void addProject(final Long userId, final CreateProjectDto projectDto) {
        if (projectRepository.findByHostIdAndNameAndExplanation(userId, projectDto.name(), projectDto.explanation()).isPresent()) {
            throw new CustomException(CustomErrorCode.DUPLICATE);
        }
        final Project project = Project.from(projectDto, userId);
        projectRepository.save(project);
    }


    public ProjectPaginationDto findProjects(final Long userId, final ProjectPaginationParam paginationParam) {
        final Page<Project> projectPage = projectRepository.findAllByHostIdAndDone(paginationParam.toPageable(), userId, paginationParam.isDone());
        return ProjectPaginationDto.from(projectPage);
    }

    @Transactional
    public void updateDone(final Long projectId, final Long userId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
        if (!project.getHost().getId().equals(userId)) {
            throw new CustomException(CustomErrorCode.INVALID_USER);
        }
        project.updateDone();
        alarmService.addAlarm(project, AlarmStatus.DONE);
    }

    @Transactional
    public void updateProject(final UpdateProjectDto projectDto, final Long projectId, final Long userId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
        if (!project.getHost().getId().equals(userId)) {
            throw new CustomException(CustomErrorCode.INVALID_USER);
        }
        project.updateProject(projectDto);
        alarmService.addAlarm(project, AlarmStatus.UPDATE_PROJECT);
    }

    @Transactional
    public void updateHost(final Long projectId, final Long userId, final Long newHostId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
        if (!project.getHost().getId().equals(userId)) {
            throw new CustomException(CustomErrorCode.INVALID_USER);
        }
        User newHost = userRepository.findById(newHostId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
        project.updateHost(newHost);
        alarmService.addAlarmWithTargetUser(project, AlarmStatus.CHANGE_HOST, newHostId);
    }

    @Transactional
    public void deleteProject(final Long projectId, final Long userId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
        if (!project.getHost().getId().equals(userId)) {
            throw new CustomException(CustomErrorCode.INVALID_USER);
        }
        projectRepository.delete(project);
    }
}
