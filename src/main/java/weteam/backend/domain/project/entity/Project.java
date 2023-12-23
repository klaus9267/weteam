package weteam.backend.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.application.common.BaseEntity;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate startedAt;

    private LocalDate endedAt;
    private boolean isDone;
}