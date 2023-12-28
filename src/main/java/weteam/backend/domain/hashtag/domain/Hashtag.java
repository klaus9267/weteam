package weteam.backend.domain.hashtag.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import weteam.backend.domain.hashtag.dto.AddHashtagDto;
import weteam.backend.domain.member.entity.Member;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String color;
    private String name;

    @Column(columnDefinition = "BIT DEFAULT 0")
    private boolean isUse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HashtagType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public static Hashtag from(AddHashtagDto hashtagDto, Long memberId) {
        return Hashtag.builder()
                      .name(hashtagDto.name())
                      .type(hashtagDto.type())
                      .color(hashtagDto.color())
                      .member(Member.builder().id(memberId).build())
                      .build();
    }
}
