package weteam.backend.domain.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.domain.user.entity.User;

@Entity(name = "profile_images")
@Getter
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
  private Long imageIdx;

  public static ProfileImage from(final Long imageIdx, final User user) {
    return ProfileImage.builder()
        .user(user)
        .imageIdx(imageIdx)
        .build();
  }

  public void updateImage(final Long imageIdx) {
    this.imageIdx = imageIdx;
  }
}
