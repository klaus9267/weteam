package weteam.backend.member.domain;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.config.domain.BaseEntity;
import weteam.backend.hashtag.domain.MemberHashtag;

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
}
