package weteam.backend.domain.user.dto;

import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;

public record UserDto(
        Long id,
        String username,
        String email,
        String organization,
        UserRole role
) {
    public static UserDto from(final User user) {
        return new UserDto(user.getId(), user.getUsername(), null, user.getOrganization(), user.getRole());
    }
}
