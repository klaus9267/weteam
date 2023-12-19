package weteam.backend.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.application.common.BaseEntity;
import weteam.backend.domain.auth.dto.JoinDto;
import weteam.backend.domain.hashtag.domain.MemberHashtag;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Member extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username, nickname;

    private String organization;

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private  List<MemberHashtag> memberHashtagList = new ArrayList<>();

    public static Member from(JoinDto joinDto) {
        return Member.builder().username(joinDto.username()).nickname(joinDto.nickname()).build();
    }
}
