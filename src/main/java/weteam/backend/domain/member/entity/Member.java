package weteam.backend.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.application.common.BaseEntity;
import weteam.backend.application.oauth.provider.ProviderType;
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
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username, nickname, organization, providerId;
    private ProviderType provider;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<MemberHashtag> memberHashtagList = new ArrayList<>();
}
