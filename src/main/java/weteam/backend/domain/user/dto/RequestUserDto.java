package weteam.backend.domain.user.dto;

public record RequestUserDto(
        String nickname,
        String username,
        String organization
) {}
