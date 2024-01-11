package weteam.backend.domain.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.swagger.SwaggerNoContent;
import weteam.backend.application.swagger.SwaggerOK;
import weteam.backend.domain.user.dto.UserDto;

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
        final UserDto user = memberService.findOneById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @SwaggerOK(summary = "내 정보 조회")
    public ResponseEntity<UserDto> readMyInfo(@AuthenticationPrincipal final UserDto user) {
        return ResponseEntity.ok(user);
    }

    @PatchMapping("{organization}")
    @Operation(summary = "사용자 소속 변경", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeOrganization(
            @PathVariable("organization") final String organization,
            @AuthenticationPrincipal final UserDto user
    ) {
        memberService.updateOrganization(user.id(), organization);
    }

    @PatchMapping("profile/{imageId}")
    @Operation(summary = "기본 프로필 사진 선택", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setDefaultProfile(
            @PathVariable("imageId") final Long imageId,
            @AuthenticationPrincipal final UserDto user
    ) {
        memberService.updateDefaultProfileImage(imageId, user.id());
    }

    @DeleteMapping
    @SwaggerNoContent(summary = "사용자 탈퇴")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@AuthenticationPrincipal final UserDto user) {
        memberService.delete(user.id());
    }
}
