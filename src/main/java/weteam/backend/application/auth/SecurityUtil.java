package weteam.backend.application.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import weteam.backend.application.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.user.dto.UserDto;

@Component
public class SecurityUtil {
    public Long getId() {
        return getCustomUserDetails().id();
    }

    private UserDto getCustomUserDetails() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new CustomException(CustomErrorCode.NOT_FOUND);
        }

        return (UserDto) authentication.getPrincipal();
    }
}
