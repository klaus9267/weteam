package weteam.backend.domain.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.swagger.SwaggerNoContent;
import weteam.backend.application.swagger.SwaggerOK;
import weteam.backend.domain.user.dto.UserWithProfileImageDto;

@RestController
@Validated
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "USER")
public class UserController {
    private final UserService memberService;
    private final SecurityUtil securityUtil;

    @GetMapping("{id}")
    @SwaggerOK(summary = "다른 사용자 조회")
    public ResponseEntity<UserWithProfileImageDto> readOne(@PathVariable("id") final Long id) {
        final UserWithProfileImageDto user = memberService.findOneById(id, false);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @SwaggerOK(summary = "내 정보 조회")
    public ResponseEntity<UserWithProfileImageDto> readMyInfo() {
        final UserWithProfileImageDto user = memberService.findOneById(securityUtil.getId(), true);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("{organization}")
    @Operation(summary = "사용자 소속 변경", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeOrganization(@PathVariable("organization") final String organization) {
        memberService.updateOrganization(organization);
    }

    @DeleteMapping
    @SwaggerNoContent(summary = "사용자 탈퇴")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser() {
        memberService.delete();
    }
}
