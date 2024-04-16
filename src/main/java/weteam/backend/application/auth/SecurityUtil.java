package weteam.backend.application.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;

@Component
public class SecurityUtil {
  public Long getId() {
    return getCustomUserDetails().id();
  }

  public CustomUser4Log getCurrentUser() {
    return getCustomUserDetails();
  }

  private CustomUser4Log getCustomUserDetails() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getName() == null) {
      throw new CustomException(CustomErrorCode.NOT_FOUND);
    }

    return (CustomUser4Log) authentication.getPrincipal();
  }
}
