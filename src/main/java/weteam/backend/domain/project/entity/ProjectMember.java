package weteam.backend.domain.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.domain.member.entity.Member;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public static ProjectMember from(Project project, Long memberId) {
        return ProjectMember.builder().project(project).member(Member.builder().id(memberId).build()).build();
    }

    public static ProjectMember from(Long projectId, Long memberId) {
        return ProjectMember.builder().project(Project.builder().id(projectId).build()).member(Member.builder().id(memberId).build()).build();
    }
}
