package weteam.backend.domain.member.dto;

public record RequestMemberDto(
        String nickname,
        String username,
        String organization
) {}
