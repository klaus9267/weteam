package weteam.backend.domain.project;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.Message;
import weteam.backend.application.handler.exception.DuplicateKeyException;
import weteam.backend.application.handler.exception.NotFoundException;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.ProjectMemberDto;
import weteam.backend.domain.project.dto.ProjectPaginationDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectMember;
import weteam.backend.domain.project.repository.ProjectMemberRepository;
import weteam.backend.domain.project.repository.ProjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public void addProject(final Long userId, final CreateProjectDto projectDto) {
        if (projectRepository.findByHostIdAndNameAndExplanation(userId, projectDto.name(), projectDto.explanation()).isPresent()) {
            throw new DuplicateKeyException(Message.DUPLICATE);
        }
        final Project project = Project.from(projectDto, userId);
        projectRepository.save(project);
    }

    public List<ProjectMemberDto> findMemberListByProject(final Long projectId) {
        final List<ProjectMember> projectMemberList = projectMemberRepository.findByProjectId(projectId);
        if (projectMemberList.isEmpty()) {
            throw new NotFoundException(Message.NOT_FOUND);
        }
        return ProjectMemberDto.from(projectMemberList);
    }

    public ProjectPaginationDto findProjects(final Long userId, final Pageable pageable) {
        final Page<Project> projectPage = projectRepository.findAllByHostId(pageable, userId);
        return ProjectPaginationDto.from(projectPage);
    }

    @Transactional
    public void acceptInvite(final Long projectId, final Long userId) {
        if (projectMemberRepository.findByProjectIdAndUserId(projectId, userId).isPresent()) {
            throw new DuplicateKeyException(Message.DUPLICATE);
        }
        projectMemberRepository.save(ProjectMember.from(projectId, userId));
    }
}
