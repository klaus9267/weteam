package weteam.backend.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.application.BaseEntity;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.UpdateProjectDto;
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
    private boolean done;

    @ManyToOne(fetch = FetchType.LAZY)
    private User host;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectUser> projectUserList = new ArrayList<>();

    public Project(CreateProjectDto projectDto, Long userId) {
        User user = new User(userId);
        ProjectUser projectUser = ProjectUser.from(user, this);

        this.name = projectDto.name();
        this.explanation = projectDto.explanation();
        this.startedAt = projectDto.startedAt();
        this.endedAt = projectDto.endedAt();
        this.host = user;
        this.projectUserList.add(projectUser);
    }

    public static Project from(CreateProjectDto projectDto, Long userId) {
        return new Project(projectDto, userId);
    }

    public void updateDone() {
        this.done = !this.isDone();
    }

    public void updateHost(final User newHost) {
        this.host = newHost;
    }

    public void updateProject(final UpdateProjectDto projectDto) {
        this.name = projectDto.name() == null ? name : projectDto.name();
        this.startedAt = projectDto.startedAt();
        this.endedAt = projectDto.startedAt();
        this.explanation = projectDto.explanation() == null ? explanation : projectDto.explanation();
    }
}
