package weteam.backend.hashtag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.config.dto.ApiMetaData;
import weteam.backend.hashtag.domain.MemberHashtag;
import weteam.backend.hashtag.dto.HashtagDto;
import weteam.backend.security.jwt.JwtUtil;
import weteam.backend.security.SecurityUtil;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/hashtags")
@RequiredArgsConstructor
@Tag(name = "Hashtag", description = "hashtag API / jwt 필수")
public class HashtagController {
    private final HashtagService hashTagService;
    private final JwtUtil jwtUtil;

    @PostMapping
    @Operation(summary = "해시태그 생성")
    public ApiMetaData<String> save(@RequestBody @Valid final HashtagDto request) {
        final Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.save(request, memberId);
        return new ApiMetaData<>("해시태그 생성 성공");
    }

    @GetMapping("/{type}")
    @Operation(summary = "해시태그 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<List<HashtagDto.Res>> findByMemberIdWithType(@PathVariable("type") final int type) {
        final Long memberId = SecurityUtil.getCurrentMemberId();
        final List<MemberHashtag> memberHashtagList = hashTagService.findByMemberIdWithType(memberId, type);
        return new ApiMetaData<>(HashtagMapper.instance.toResList(memberHashtagList));
    }


    @PatchMapping("/{memberHashtagId}")
    @Operation(summary = "해시태그 활성화/비활성화")
    public ApiMetaData<String> updateUse(@PathVariable("memberHashtagId") final Long memberHashtagId) {
        final Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.updateUse(memberHashtagId, memberId);
        return new ApiMetaData<>("해시태그 활성화/비활성화 성공");
    }

    @DeleteMapping("/{memberHashtagId}")
    @Operation(summary = "해시태그 삭제")
    public ApiMetaData<String> delete(@PathVariable("memberHashtagId") final Long memberHashtagId) {
        final Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.delete(memberHashtagId, memberId);
        return new ApiMetaData<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/all")
    @Operation(summary = "해시태그 전체 삭제")
    public ApiMetaData<?> deleteAll() {
        final Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.deleteAllByMemberId(memberId);
        return new ApiMetaData<>(HttpStatus.NO_CONTENT);
    }
}
