package weteam.backend.application.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.user.entity.User;

@Component
public class SecurityUtil {
  public Long getCurrentUserId() {
    return getCustomUserDetails().getId();
  }

  public User getCurrentUser() {
    return getCustomUserDetails();
  }

  private User getCustomUserDetails() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getName() == null) {
      throw new CustomException(ErrorCode.NOT_FOUND);
    }

    return (User) authentication.getPrincipal();
  }
}
