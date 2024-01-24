package weteam.backend.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.CustomErrorCode;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.alarm.AlarmService;
import weteam.backend.domain.alarm.AlarmStatus;
import weteam.backend.domain.project.dto.ProjectMemberDto;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.project.param.UpdateProjectRoleParam;
import weteam.backend.domain.project.repository.ProjectMemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectUserService {
    private final ProjectMemberRepository projectMemberRepository;
    private final AlarmService alarmService;
    private final SecurityUtil securityUtil;

    public List<ProjectMemberDto> findUsersByProjectId(final Long projectId) {
        final List<ProjectUser> projectUserList = projectMemberRepository.findByProjectId(projectId);
        if (projectUserList.isEmpty()) {
            throw new CustomException(CustomErrorCode.NOT_FOUND);
        }
        return ProjectMemberDto.from(projectUserList);
    }

    @Transactional
    public void acceptInvite(final Long projectId) {
        if (projectMemberRepository.findByProjectIdAndUserId(projectId, securityUtil.getId()).isPresent()) {
            throw new CustomException(CustomErrorCode.DUPLICATE);
        }
        projectMemberRepository.save(ProjectUser.from(projectId, securityUtil.getId()));
        alarmService.addAlarmWithTargetUser(projectId, AlarmStatus.JOIN, securityUtil.getId());
    }

    @Transactional
    public void updateProjectRole(final UpdateProjectRoleParam param) {
        ProjectUser projectUser = projectMemberRepository.findByProjectIdAndUserId(param.getProjectId(), securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
        projectUser.updateRole(param.getRole());
    }

    @Transactional
    public void kickUser(final Long projectId, final Long targetUserId) {
        if (!projectMemberRepository.checkHost(securityUtil.getId())) {
            throw new CustomException(CustomErrorCode.INVALID_USER, "호스트가 아닙니다.");
        }
        projectMemberRepository.deleteByUserId(targetUserId);
        alarmService.addAlarmWithTargetUser(projectId, AlarmStatus.KICK, targetUserId);
    }
}
