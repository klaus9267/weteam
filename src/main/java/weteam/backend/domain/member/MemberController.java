package weteam.backend.domain.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.oauth.PrincipalDetails;
import weteam.backend.domain.member.dto.MemberDto;
import weteam.backend.domain.member.entity.Member;

@RestController
@Validated
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "MEMBER")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("{id}")
    @Operation(summary = "다른 사용자 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<MemberDto> findById(@PathVariable("id") final Long id) {
        final Member member = memberService.findOneById(id);
        return ResponseEntity.ok(MemberDto.from(member));
    }

    @GetMapping
    @Operation(summary = "내 정보 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<MemberDto> findMyInfo(@AuthenticationPrincipal final PrincipalDetails principalDetails) {
        final Member member = memberService.findOneById(principalDetails.getMember().id());
        return ResponseEntity.ok(MemberDto.from(member));
    }

    @PatchMapping("{organization}")
    @Operation(summary = "사용자 소속 변경", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrganization(
            @PathVariable("organization") final String organization,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        memberService.updateOrganization(principalDetails.getMember().id(), organization);
    }

    @DeleteMapping
    @Operation(summary = "사용자 탈퇴", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@AuthenticationPrincipal final PrincipalDetails principalDetails) {
        memberService.delete(principalDetails.getMember().id());
    }
}
