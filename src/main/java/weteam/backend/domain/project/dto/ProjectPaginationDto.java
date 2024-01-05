package weteam.backend.domain.project.dto;

import org.springframework.data.domain.Page;
import weteam.backend.domain.project.entity.Project;

import java.util.List;

public record ProjectPaginationDto(
        int totalPages,
        int totalElements,
        List<ProjectDto> projectList
) {
    public static ProjectPaginationDto from(Page<Project> projectPage) {
        return new ProjectPaginationDto(projectPage.getTotalPages(), projectPage.getNumberOfElements(), ProjectDto.from(projectPage.getContent()));
    }
}