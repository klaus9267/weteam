package weteam.backend.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import weteam.backend.domain.member.domain.Member;
import weteam.backend.domain.hashtag.dto.HashtagDto;

import java.util.List;


@Builder
@AllArgsConstructor
public record MemberDto(
        Long id,
        String nickname,
        String username,
        String organization,
        List<HashtagDto> hashtagList) {
    public static MemberDto from(Member member) {
        return MemberDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .username(member.getUsername())
                        .organization(member.getOrganization())
                        .build();
    }
}
