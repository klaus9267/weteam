package weteam.backend.domain.user;

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
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.user.entity.User;

@RestController
@Validated
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "MEMBER")
public class UserController {
    private final UserService memberService;

    @GetMapping("{id}")
    @Operation(summary = "다른 사용자 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<UserDto> findById(@PathVariable("id") final Long id) {
        final User user = memberService.findOneById(id);
        return ResponseEntity.ok(UserDto.from(user));
    }

    @GetMapping
    @Operation(summary = "내 정보 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<UserDto> findMyInfo(@AuthenticationPrincipal final PrincipalDetails principalDetails) {
        final User user = memberService.findOneById(principalDetails.getMember().id());
        return ResponseEntity.ok(UserDto.from(user));
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
