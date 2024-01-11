package weteam.backend.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.application.BaseEntity;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name, explanation;
    private LocalDate startedAt, endedAt;

    @Column(columnDefinition = "boolean default false")
    private boolean isDone;

    @ManyToOne(fetch = FetchType.LAZY)
    private User host;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectMember> projectMemberList = new ArrayList<>();

    public Project(CreateProjectDto projectDto, Long userId) {
        User user = User.from(userId);
        ProjectMember projectMember = ProjectMember.from(user, this);

        this.name = projectDto.name();
        this.explanation = projectDto.explanation();
        this.startedAt = projectDto.startedAt();
        this.endedAt = projectDto.endedAt();
        this.host = user;
        this.projectMemberList.add(projectMember);
    }

    public static Project from(CreateProjectDto projectDto, Long userId) {
        return new Project(projectDto, userId);
    }
}
