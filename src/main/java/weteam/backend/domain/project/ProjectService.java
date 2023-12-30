package weteam.backend.domain.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.Message;
import weteam.backend.application.handler.exception.DuplicateKeyException;
import weteam.backend.application.handler.exception.NotFoundException;
import weteam.backend.domain.project.dto.ProjectMemberDto;
import weteam.backend.domain.project.dto.RequestProjectDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectMember;
import weteam.backend.domain.project.repository.ProjectMemberRepository;
import weteam.backend.domain.project.repository.ProjectRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public void save(Long memberId, RequestProjectDto projectDto) {
        Project project = projectRepository.save(projectDto.toEntity());
        ProjectMember projectMember = ProjectMember.from(project, memberId);
        projectMemberRepository.save(projectMember);
    }

    public List<ProjectMemberDto> findMemberListByProject(Long projectId) {
        List<ProjectMember> projectMemberList = projectMemberRepository.findByProjectId(projectId);
        if (projectMemberList.isEmpty()) {
            throw new NotFoundException(Message.NOT_FOUND);
        }
        return ProjectMemberDto.from(projectMemberList);
    }

    public void acceptInvite(Long projectId, Long memberId) {
        if (projectMemberRepository.findByProjectIdAndUserId(projectId, memberId).isPresent()) {
            throw new DuplicateKeyException(Message.DUPLICATE);
        }
        projectMemberRepository.save(ProjectMember.from(projectId, memberId));
    }
}
