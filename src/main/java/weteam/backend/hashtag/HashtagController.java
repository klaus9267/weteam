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
    public ResponseEntity<Message<?>> create(@RequestBody @Valid HashtagDto request) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.create(request, memberId);

        Message<?> message = Message.builder()
                                    .result(true)
                                    .httpStatus(HttpStatus.OK)
                                    .message("해시태그 생성 성공")
                                    .build();
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{type}")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "해시태그 조회",
               responses = {@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
               })
    public ResponseEntity<Message<List<HashtagDto.Res>>> findByMemberIdWithType(@PathVariable("type") int type) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<MemberHashtag> memberHashtagList = hashTagService.findByMemberIdWithType(memberId, type);
        List<HashtagDto.Res> resList = HashtagMapper.instance.toResList(memberHashtagList);

        Message<List< HashtagDto.Res>> message = Message.<List<HashtagDto.Res>>builder()
                                                       .result(true)
                                                       .httpStatus(HttpStatus.OK)
                                                       .message("다른 사용자 정보 조회 성공")
                                                       .data(resList)
                                                       .build();
        return ResponseEntity.ok(message);
    }


    @PatchMapping("/{memberHashtagId}")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "해시태그 활성화/비활성화")
    public ResponseEntity<Message<?>> updateUse(@PathVariable("memberHashtagId") Long memberHashtagId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.updateUse(memberHashtagId, memberId);

        Message<?> message = Message.builder()
                                    .result(true)
                                    .httpStatus(HttpStatus.OK)
                                    .message("해시태그 활성화/비활성화 성공")
                                    .build();
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{memberHashtagId}")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "해시태그 삭제")
    public ResponseEntity<Message<?>> delete(@PathVariable("memberHashtagId") Long memberHashtagId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.delete(memberHashtagId, memberId);

        Message<?> message = Message.builder()
                                    .result(true)
                                    .httpStatus(HttpStatus.OK)
                                    .message("해시태그 삭제 성공")
                                    .build();
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/all")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "해시태그 전체 삭제")
    public ResponseEntity<Message<?>> deleteAl() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        hashTagService.deleteAllByMemberId(memberId);

        Message<?> message = Message.builder()
                                    .result(true)
                                    .httpStatus(HttpStatus.OK)
                                    .message("해시태그 전체 삭제 성공")
                                    .build();
        return ResponseEntity.ok(message);
    }
}
