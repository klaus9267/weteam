package weteam.backend.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.CustomErrorCode;
import weteam.backend.application.auth.SecurityUtil;
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
            throw new CustomException(CustomErrorCode.NOT_FOUND);
        }
        return ProjectUserDto.from(projectUserList);
    }

    @Transactional
    public void acceptInvite(final Long projectId) {
        Project project= projectRepository.findById(projectId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
        project.getProjectUserList().forEach(projectUser -> {
            if (projectUser.getUser().getId().equals(securityUtil.getId())) {
                throw new CustomException(CustomErrorCode.DUPLICATE);
            }
        });
        ProjectUser projectUser = projectUserRepository.save(ProjectUser.from(project, securityUtil.getId()));
        alarmService.addAlarmWithTargetUser(projectUser.getProject(), AlarmStatus.JOIN, securityUtil.getId());
    }

    @Transactional
    public void updateProjectRole(final UpdateProjectRoleParam param) {
        ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(param.getProjectId(), securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
        projectUser.updateRole(param.getRole());
    }

    @Transactional
    public void kickUser(final Long projectUserId) {
        ProjectUser projectUser = projectUserRepository.findById(projectUserId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
        if (!projectUser.isEnable()) {
            throw new CustomException(CustomErrorCode.BAD_REQUEST, "이미 팀플을 나간 유저입니다.");
        }
        if (!projectUser.getProject().getHost().getId().equals(securityUtil.getId())) {
            throw new CustomException(CustomErrorCode.BAD_REQUEST, "호스트가 아닙니다.");
        }
        projectUser.disable();
        alarmService.addAlarmWithTargetUser(projectUser.getProject(), AlarmStatus.KICK, projectUserId);
    }
}
