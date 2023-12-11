package weteam.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import weteam.backend.hashtag.dto.HashtagDto;
import weteam.backend.member.domain.Member;

import java.util.ArrayList;
import java.util.List;


@Builder
@AllArgsConstructor
public class MemberDto {
        @Schema(description = "사용자 pk", nullable = false, example = "7")
        private Long id;

        @Schema(description = "사용자 닉네임", nullable = false, example = "인덕대 손흥민")
        private String nickname;

        @Schema(description = "사용자 성명", nullable = false, example = "김성현")
        private String username;

        @Schema(description = "사용자 소속", nullable = false, example = "인덕대 컴퓨터소프트웨어학과")
        private String organization;

        @Schema(description = "사용자 해시태그 리스트", nullable = false)
        private List<HashtagDto> hashtagList = new ArrayList<>();

    public static MemberDto from(Member member) {
        return MemberDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .username(member.getUsername())
                        .organization(member.getOrganization())
                        .build();
    }
}
