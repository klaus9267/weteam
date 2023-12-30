package weteam.backend.domain.member.dto;

import weteam.backend.domain.member.entity.Member;
import weteam.backend.domain.member.entity.MemberRole;

public record MemberDto(
        Long id,
        String nickname,
        String username,
        String organization,
        MemberRole role
) {
    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getNickname(), member.getUsername(), member.getOrganization(), member.getRole());
    }
}
