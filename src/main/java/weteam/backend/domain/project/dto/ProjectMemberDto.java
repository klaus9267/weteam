package weteam.backend.domain.project.dto;

import lombok.Builder;
import lombok.Getter;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class ProjectMemberDto {
    private Long id;
    private String role;
    private UserDto user;

    public static ProjectMemberDto from(ProjectUser projectUser) {
        return ProjectMemberDto.builder()
                               .id(projectUser.getId())
                               .role(projectUser.getRole())
                               .user(UserDto.from(projectUser.getUser()))
                               .build();
    }

    public static List<ProjectMemberDto> from(List<ProjectUser> projectUserList) {
        return projectUserList.stream().map(ProjectMemberDto::from).collect(Collectors.toList());
    }
}
