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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectUserService {
    private final ProjectUserRepository projectUserRepository;
    private final ProjectRepository projectRepository;
    private final AlarmService alarmService;
    private final SecurityUtil securityUtil;

    public List<ProjectUserDto> findUsersByProjectId(final Long projectId) {
        final List<ProjectUser> projectUserList = projectUserRepository.findByProjectId(projectId);
        if (projectUserList.isEmpty()) {
            throw new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER);
        }
        return ProjectUserDto.from(projectUserList);
    }

    @Transactional
    public void acceptInvite(final Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
        project.getProjectUserList().forEach(projectUser -> {
            if (projectUser.getUser().getId().equals(securityUtil.getId())) {
                throw new CustomException(CustomErrorCode.DUPLICATE, "이미 참가한 프로젝트입니다.");
            }
        });
        ProjectUser projectUser = projectUserRepository.save(ProjectUser.from(project, securityUtil.getId()));
        alarmService.addAlarmWithTargetUser(projectUser.getProject(), AlarmStatus.JOIN, securityUtil.getId());
    }

    @Transactional
    public void updateProjectRole(final UpdateProjectRoleParam param) {
        ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(param.getProjectId(), securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT_USER));
        projectUser.updateRole(param.getRole());
    }

    @Transactional
    public void kickUsers(final List<Long> projectUserIdList) {
        List<ProjectUser> projectUser = projectUserRepository.findAllById(projectUserIdList).stream().filter(ProjectUser::isEnable).toList();
        projectUser.forEach(ProjectUser::disable);
        alarmService.addAlarmList(projectUser, AlarmStatus.KICK);
    }

    @Transactional
    public void exitProject(final Long projectId) {
        ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(projectId, securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
        projectUser.disable();
        alarmService.addAlarmWithTargetUser(projectUser.getProject(), AlarmStatus.EXIT, securityUtil.getId());
    }
}
