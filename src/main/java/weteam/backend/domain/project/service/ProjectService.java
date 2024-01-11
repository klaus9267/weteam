package weteam.backend.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.Message;
import weteam.backend.application.handler.exception.DuplicateKeyException;
import weteam.backend.domain.common.pagination.param.ProjectPaginationParam;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.ProjectPaginationDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.repository.ProjectMemberRepository;
import weteam.backend.domain.project.repository.ProjectRepository;

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


    public ProjectPaginationDto findProjects(final Long userId, final ProjectPaginationParam paginationParam) {
        final Page<Project> projectPage = projectRepository.findAllByHostIdAndDone(paginationParam.toPageable(), userId, paginationParam.isDone());
        return ProjectPaginationDto.from(projectPage);
    }
}
