package weteam.backend.domain.project.dto;

import lombok.Builder;
import lombok.Getter;
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.project.entity.ProjectMember;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class ProjectMemberDto {
    private Long id;
    private String role;
    private UserDto member;

    public static ProjectMemberDto from(ProjectMember projectMember) {
        return ProjectMemberDto.builder()
                               .id(projectMember.getId())
                               .role(projectMember.getRole())
                               .member(UserDto.from(projectMember.getUser()))
                               .build();
    }

    public static List<ProjectMemberDto> from(List<ProjectMember> projectMemberList) {
        return projectMemberList.stream().map(ProjectMemberDto::from).collect(Collectors.toList());
    }
}
