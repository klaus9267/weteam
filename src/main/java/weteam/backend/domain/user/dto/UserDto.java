package weteam.backend.domain.user.dto;

import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;

public record UserDto(
    Long id,
    String username,
    String email,
    String deviceToken,
    String organization,
    String uid,
    UserRole role
) {
  public static UserDto from(final User user) {
    return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getDeviceToken(), user.getOrganization(), user.getUid(), user.getRole());
  }
}
