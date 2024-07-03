package weteam.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;

@Getter
@AllArgsConstructor
public class UserWithProfileImageDtoV2 {
  private final Long id;
  private final String username;
  private final String email;
  private final String organization;
  private final String introduction;
  private final boolean receivePermission;
  private final UserRole role;
  private final Long profileId;

  private UserWithProfileImageDtoV2(final User user, final boolean isCurrentUser) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = isCurrentUser ? user.getEmail() : null;
    this.organization = user.getOrganization();
    this.introduction = user.getIntroduction();
    this.receivePermission = user.isReceivePermission();
    this.role = user.getRole();
    this.profileId = user.getProfileImage() == null ? null : user.getProfileImage().getImageIdx();
  }

  public static UserWithProfileImageDtoV2 from(final User user, final boolean isCurrentUser) {
    return new UserWithProfileImageDtoV2(user, isCurrentUser);
  }
}
