package weteam.backend.domain.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.CustomErrorCode;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomException;
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
    private final SecurityUtil securityUtil;

    @GetMapping("{id}")
    @SwaggerOK(summary = "다른 사용자 조회")
    public ResponseEntity<UserDto> readOne(@PathVariable("id") final Long id) {
        final UserDto user = memberService.findOneById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @SwaggerOK(summary = "내 정보 조회")
    public ResponseEntity<UserDto> readMyInfo() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = (UserDto) authentication.getPrincipal();
        return ResponseEntity.ok(userDto);
    }


    @PatchMapping("{organization}")
    @Operation(summary = "사용자 소속 변경", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeOrganization(@PathVariable("organization") final String organization    ) {
        memberService.updateOrganization(organization);
    }

    @DeleteMapping
    @SwaggerNoContent(summary = "사용자 탈퇴")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser() {
        memberService.delete();
    }
}
