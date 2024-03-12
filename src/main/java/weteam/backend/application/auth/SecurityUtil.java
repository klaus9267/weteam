package weteam.backend.application.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;

@Component
public class SecurityUtil {
  public Long getId() {
    return getCustomUserDetails().id();
  }

  private CustomUser4Log getCustomUserDetails() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getName() == null) {
      throw new CustomException(CustomErrorCode.NOT_FOUND);
    }

    return (CustomUser4Log) authentication.getPrincipal();
  }

  public String resolveToken(final String header) {
    return StringUtils.hasText(header) && header.startsWith("Bearer ") ? header.substring(7) : null;
  }
}
