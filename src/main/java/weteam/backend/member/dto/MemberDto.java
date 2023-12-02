package weteam.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import weteam.backend.hashtag.dto.HashtagDto;

import java.util.ArrayList;
import java.util.List;


public class MemberDto {
    @Getter
    @Builder
    @Schema(name = "MemberDto.Res")
    public static class Res{
        @Schema(description = "사용자 pk", nullable = false, example = "7")
        private String id;

        @Schema(description = "사용자 닉네임", nullable = false, example = "인덕대 손흥민")
        private String nickname;

        @Schema(description = "사용자 성명", nullable = false, example = "김성현")
        private String username;

        @Schema(description = "사용자 소속", nullable = false, example = "인덕대 컴퓨터소프트웨어학과")
        private String organization;

        @Schema(description = "사용자 해시태그 리스트", nullable = false)
        private List<HashtagDto.Res> hashtagList = new ArrayList<>();
    }
}
