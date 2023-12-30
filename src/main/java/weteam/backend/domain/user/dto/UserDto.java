package weteam.backend.domain.user.dto;

import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;

public record UserDto(
        Long id,
        String nickname,
        String username,
        String organization,
        UserRole role
) {
    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getNickname(), user.getUsername(), user.getOrganization(), user.getRole());
    }
}
