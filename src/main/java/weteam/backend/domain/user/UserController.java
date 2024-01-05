package weteam.backend.domain.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.oauth.PrincipalDetails;
import weteam.backend.application.swagger.SwaggerNoContent;
import weteam.backend.application.swagger.SwaggerOK;
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.user.entity.User;

@RestController
@Validated
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "USER")
public class UserController {
    private final UserService memberService;

    @GetMapping("{id}")
    @SwaggerOK(summary = "다른 사용자 조회")
    public ResponseEntity<UserDto> readOne(@PathVariable("id") final Long id) {
        final User user = memberService.findOneById(id);
        return ResponseEntity.ok(UserDto.from(user));
    }

    @GetMapping
    @SwaggerOK(summary = "내 정보 조회")
    public ResponseEntity<UserDto> readMyInfo(@AuthenticationPrincipal final PrincipalDetails principalDetails) {
        final User user = memberService.findOneById(principalDetails.getUser().id());
        return ResponseEntity.ok(UserDto.from(user));
    }

    @PatchMapping("{organization}")
    @Operation(summary = "사용자 소속 변경", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeOrganization(
            @PathVariable("organization") final String organization,
            @AuthenticationPrincipal final PrincipalDetails principalDetails
    ) {
        memberService.updateOrganization(principalDetails.getUser().id(), organization);
    }

    @PatchMapping
    @Operation(summary = "사용자 소속 변경", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeInfo(@AuthenticationPrincipal final PrincipalDetails principalDetails) {
        memberService.updateOrganization(principalDetails.getUser().id(), organization);
    }

    @DeleteMapping
    @SwaggerNoContent(summary = "사용자 탈퇴")
    public void deleteMember(@AuthenticationPrincipal final PrincipalDetails principalDetails) {
        memberService.delete(principalDetails.getUser().id());
    }
}
