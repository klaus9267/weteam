package weteam.backend.domain.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.common.ApiMetaData;
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
    public ApiMetaData<MemberDto> findById(@PathVariable("id") Long id) {
        final MemberDto member = memberService.findProfile(id);
        return new ApiMetaData<>(member);
    }

    @GetMapping
    @Operation(summary = "내 정보 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<MemberDto> findMyInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        final MemberDto member = memberService.findProfile(principalDetails.getMember().id());
        return new ApiMetaData<>(member);
    }

    @PatchMapping("{organization}")
    @Operation(summary = "사용자 소속 변경", description = "응답 없음")
    @ResponseStatus(HttpStatus.OK)
    public void updateOrganization(
            @PathVariable("organization") String organization,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        memberService.updateOrganization(principalDetails.getMember().id(), organization);
    }
}
