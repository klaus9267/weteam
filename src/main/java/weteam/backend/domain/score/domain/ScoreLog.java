package weteam.backend.domain.score.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import weteam.backend.domain.member.entity.Member;
import weteam.backend.domain.project.entity.Project;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScoreLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member scoredMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member scorerMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project projectList;
}
