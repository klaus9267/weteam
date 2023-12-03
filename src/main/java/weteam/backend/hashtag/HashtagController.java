package weteam.backend.hashtag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.config.dto.Message;
import weteam.backend.hashtag.domain.MemberHashtag;
import weteam.backend.hashtag.dto.HashtagDto;
import weteam.backend.hashtag.mapper.HashtagMapper;
import weteam.backend.security.util.JwtUtil;
import weteam.backend.security.util.SecurityUtil;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/hashtags")
@RequiredArgsConstructor
@Tag(name = "Hashtag", description = "hashtag API / jwt 필수")
public class HashtagController {
    private final HashtagService hashTagService;
    private final JwtUtil jwtUtil;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "해시태그 생성")
    public Message<String> create(@RequestBody @Valid HashtagDto request) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.create(request, memberId);
        return new Message<>("해시태그 생성 성공");
    }

    @GetMapping("/{type}")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "해시태그 조회",
               responses = {@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
               })
    public Message<List<HashtagDto.Res>> findByMemberIdWithType(@PathVariable("type") int type) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<MemberHashtag> memberHashtagList = hashTagService.findByMemberIdWithType(memberId, type);
        List<HashtagDto.Res> resList = HashtagMapper.instance.toResList(memberHashtagList);
        return new Message<>(resList);
    }


    @PatchMapping("/{memberHashtagId}")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "해시태그 활성화/비활성화")
    public Message<String> updateUse(@PathVariable("memberHashtagId") Long memberHashtagId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.updateUse(memberHashtagId, memberId);
        return new Message<>("해시태그 활성화/비활성화 성공");
    }

    @DeleteMapping("/{memberHashtagId}")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "해시태그 삭제")
    public Message<String> delete(@PathVariable("memberHashtagId") Long memberHashtagId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.delete(memberHashtagId, memberId);
        return new Message<>("해시태그 삭제 성공");
    }

    @DeleteMapping("/all")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "해시태그 전체 삭제")
    public Message<String> deleteAl() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.deleteAllByMemberId(memberId);
        return new Message<>("해시태그 전체 삭제 성공");
    }
}
