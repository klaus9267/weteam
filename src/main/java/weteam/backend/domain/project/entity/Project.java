package weteam.backend.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.application.BaseEntity;

import java.time.LocalDate;

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
    private String name, subtitle;
    private LocalDate startedAt, endedAt;

    @Column(columnDefinition = "boolean default false")
    private boolean isDone;
}
