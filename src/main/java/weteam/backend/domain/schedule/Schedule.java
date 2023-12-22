package weteam.backend.domain.schedule;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import weteam.backend.domain.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime alarm;
    private Integer repeatType;
    private String place;
    private String memo;
    private String color;

    @ColumnDefault("false")
    private boolean isDone;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
