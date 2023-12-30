package weteam.backend.domain.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.domain.user.entity.User;

@Entity(name = "project_users")
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
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public static ProjectMember from(Project project, Long userId) {
        return ProjectMember.builder().project(project).user(User.builder().id(userId).build()).build();
    }

    public static ProjectMember from(Long projectId, Long userId) {
        return ProjectMember.builder().project(Project.builder().id(projectId).build()).user(User.builder().id(userId).build()).build();
    }
}
