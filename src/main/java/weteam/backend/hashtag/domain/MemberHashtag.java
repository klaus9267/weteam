package weteam.backend.hashtag.domain;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.member.domain.Member;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MemberHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BIT DEFAULT 0")
    private boolean isUse;

    private String color;

    @ManyToOne(fetch =  FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch =  FetchType.LAZY)
    private Hashtag hashtag;
}
