package weteam.backend.domain.hashtag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.common.ApiMetaData;
import weteam.backend.application.oauth.PrincipalDetails;
import weteam.backend.domain.hashtag.dto.AddHashtagDto;
import weteam.backend.domain.hashtag.dto.HashtagDto;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/hashtags")
@RequiredArgsConstructor
@Tag(name = "Hashtag")
public class HashtagController {
    private final HashtagService hashTagService;

    @PostMapping
    @Operation(summary = "해시태그 생성", description = "응답 없음")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(
            @RequestBody @Valid final AddHashtagDto request,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        hashTagService.create(request, principalDetails.getMember().id());
    }

    @GetMapping("/{type}")
    @Operation(summary = "해시태그 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<List<HashtagDto>> findByMemberIdWithType(
            @PathVariable("type") final String type,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return new ApiMetaData<>(hashTagService.findByMemberIdWithType(principalDetails.getMember().id(), type));
    }


    @PatchMapping("/{memberHashtagId}")
    @Operation(summary = "해시태그 활성화/비활성화", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUse(
            @PathVariable("memberHashtagId") final Long memberHashtagId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        hashTagService.updateUse(memberHashtagId, principalDetails.getMember().id());
    }

    @DeleteMapping("/{memberHashtagId}")
    @Operation(summary = "해시태그 삭제", description = "응답 없음")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @PathVariable("memberHashtagId") final Long memberHashtagId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        hashTagService.delete(memberHashtagId, principalDetails.getMember().id());
    }

    @DeleteMapping("/all")
    @Operation(summary = "해시태그 전체 삭제", description = "응답 없음")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAll(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        hashTagService.deleteAllByMemberId(principalDetails.getMember().id());
    }
}
