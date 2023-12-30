package weteam.backend.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity(name = "project_tasks")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false)
    private String name;

    @Column( nullable = false)
    private Date startedAt;

    @Column( nullable = true)
    private Date endedAt;

    @ColumnDefault("false")
    private boolean isDone;
}
