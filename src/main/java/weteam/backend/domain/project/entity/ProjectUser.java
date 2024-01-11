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
public class ProjectUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public static ProjectUser from(final User user, final Project project) {
        return ProjectUser.builder().user(user).project(project).build();
    }

    public static ProjectUser from(Long projectId, Long userId) {
        return ProjectUser.builder().project(Project.builder().id(projectId).build()).user(User.builder().id(userId).build()).build();
    }

    public void updateRole(final String role) {
        this.role = role;
    }
}
