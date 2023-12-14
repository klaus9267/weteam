package weteam.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import weteam.backend.domain.hashtag.dto.HashtagDto;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RequestMemberDto {
    @Schema(description = "사용자 닉네임", nullable = false, example = "인덕대 손흥민")
    private String nickname;

    @Schema(description = "사용자 성명", nullable = false, example = "김성현")
    private String username;

    @Schema(description = "사용자 소속", nullable = false, example = "인덕대 컴퓨터소프트웨어학과")
    private String organization;

    @Schema(description = "사용자 해시태그 리스트", nullable = false)
    private List<HashtagDto> hashtagList = new ArrayList<>();
}
