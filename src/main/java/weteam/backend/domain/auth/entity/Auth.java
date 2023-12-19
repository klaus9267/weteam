package weteam.backend.domain.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import weteam.backend.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Auth {
    @Id
    private Long id;

    @Column(updatable = false)
    private String uid;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Member member;

    public Auth(Member member, String password, String uid) {
        this.member = member;
        this.password = password;
        this.uid = uid;
    }
}
