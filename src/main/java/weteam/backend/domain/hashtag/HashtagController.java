package weteam.backend.domain.hashtag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.common.ApiMetaData;
import weteam.backend.application.security.SecurityUtil;
import weteam.backend.domain.hashtag.dto.AddHashtagDto;
import weteam.backend.domain.hashtag.dto.HashtagDto;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/hashtags")
@RequiredArgsConstructor
@Tag(name = "Hashtag", description = "hashtag API / jwt 필수")
public class HashtagController {
    private final HashtagService hashTagService;

    @PostMapping
    @Operation(summary = "해시태그 생성", description = "응답 없음")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid final AddHashtagDto request) {
        final Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.create(request, memberId);
    }

    @GetMapping("/{type}")
    @Operation(summary = "해시태그 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<List<HashtagDto>> findByMemberIdWithType(@PathVariable("type") final String type) {
        final Long memberId = SecurityUtil.getCurrentMemberId();
        return new ApiMetaData<>(hashTagService.findByMemberIdWithType(memberId, type));
    }


    @PatchMapping("/{memberHashtagId}")
    @Operation(summary = "해시태그 활성화/비활성화", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUse(@PathVariable("memberHashtagId") final Long memberHashtagId) {
        final Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.updateUse(memberHashtagId, memberId);
    }

    @DeleteMapping("/{memberHashtagId}")
    @Operation(summary = "해시태그 삭제", description = "응답 없음")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("memberHashtagId") final Long memberHashtagId) {
        final Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.delete(memberHashtagId, memberId);
    }

    @DeleteMapping("/all")
    @Operation(summary = "해시태그 전체 삭제", description = "응답 없음")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAll() {
        final Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.deleteAllByMemberId(memberId);
    }
}
