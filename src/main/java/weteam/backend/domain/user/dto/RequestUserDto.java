package weteam.backend.domain.user.dto;

public record RequestUserDto(
        String username,
        String organization,
        String introduction
) {}
