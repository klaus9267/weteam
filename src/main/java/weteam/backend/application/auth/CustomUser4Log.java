package weteam.backend.application.auth;

import weteam.backend.domain.user.entity.User;

public record CustomUser4Log(
    Long id,
    String username,
    String uid
) {
  public static CustomUser4Log from(final User user) {
    return new CustomUser4Log(user.getId(), user.getUsername(), user.getUid());
  }
}
