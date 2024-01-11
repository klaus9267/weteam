package weteam.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity(name = "profile_images")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileImage {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    @Column(nullable = false)
    private String url;

    public static ProfileImage from(final Long imageId, final User user) {
        return ProfileImage.builder().user(user)
                           .url(String.valueOf(imageId))
                           .build();
    }
}
