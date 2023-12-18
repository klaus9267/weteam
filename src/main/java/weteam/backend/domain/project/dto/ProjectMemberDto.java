package weteam.backend.domain.project.dto;

import lombok.Builder;
import lombok.Getter;
import weteam.backend.domain.member.dto.MemberDto;
import weteam.backend.domain.project.domain.ProjectMember;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class ProjectMemberDto {
    private Long id;
    private String role;
    private MemberDto member;

    public static ProjectMemberDto from(ProjectMember projectMember) {
        return ProjectMemberDto.builder()
                               .id(projectMember.getId())
                               .role(projectMember.getRole())
                               .member(MemberDto.from(projectMember.getMember()))
                               .build();
    }

    public static List<ProjectMemberDto> from(List<ProjectMember> projectMemberList) {
        return projectMemberList.stream().map(ProjectMemberDto::from).collect(Collectors.toList());
    }
}
