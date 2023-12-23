package weteam.backend.domain.member.dto;

import lombok.Builder;
import weteam.backend.domain.hashtag.dto.HashtagDto;
import weteam.backend.domain.member.entity.Member;
import weteam.backend.domain.member.entity.MemberRole;

import java.util.List;


@Builder
public record MemberDto(
        Long id,
        String nickname,
        String username,
        String organization,
        MemberRole role) {
    public static MemberDto from(Member member) {
        return MemberDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .username(member.getUsername())
                        .organization(member.getOrganization())
                        .role(member.getRole())
                        .build();
    }
}
