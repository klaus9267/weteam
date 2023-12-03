package weteam.backend.schedule.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import weteam.backend.member.domain.Member;
import weteam.backend.schedule.dto.MemberScheduleDto;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MemberSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
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

    public MemberScheduleDto.Res toDto() {
        return new MemberScheduleDto.Res(id, title, startedAt, endedAt, place, alarm, repeatType, memo, color);
    }
}
