package weteam.backend.domain.meeting;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.application.BaseEntity;
import weteam.backend.domain.project.entity.Project;

@Entity(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Meeting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
}
