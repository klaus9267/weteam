package weteam.backend.domain.score.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import weteam.backend.domain.member.domain.Member;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Score {
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    private Member memberId;

    @Column(nullable = false)
    private int total;

    @Column(nullable = false)
    private int count;
}
