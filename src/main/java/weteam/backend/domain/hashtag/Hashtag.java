package weteam.backend.domain.hashtag;

import jakarta.persistence.*;
import lombok.Getter;
import weteam.backend.domain.user.entity.User;

@Entity(name = "hashtags")
@Getter
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
