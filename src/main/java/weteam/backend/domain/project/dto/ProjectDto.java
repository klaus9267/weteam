package weteam.backend.domain.project.dto;

import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.dto.UserDto;

import java.util.List;

public record ProjectDto(
        Long id,
        String name,
        String explanation,
        int headCount,
        boolean done,
        UserDto host
) {
    public static ProjectDto from(Project project) {
        return new ProjectDto(project.getId(), project.getName(), project.getExplanation(), project.getProjectUserList().size(), project.isDone(), UserDto.from(project.getHost()));
    }

    public static List<ProjectDto> from(List<Project> projectList) {
        return projectList.stream().map(ProjectDto::from).toList();
    }
}
