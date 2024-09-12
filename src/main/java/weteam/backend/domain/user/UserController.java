package weteam.backend.domain.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.domain.common.swagger.SwaggerNoContent;
import weteam.backend.domain.common.swagger.SwaggerOK;
import weteam.backend.domain.user.dto.RequestUserDto;
import weteam.backend.domain.user.dto.UserWithProfileImageDto;
import weteam.backend.domain.user.dto.UserWithProfileImageDtoV2;
import weteam.backend.domain.user.entity.User;

@RestController
@Validated
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "USER")
public class UserController {
  private final UserFacade userFacade;
  private final SecurityUtil securityUtil;

  @GetMapping("{id}")
  @SwaggerOK(summary = "다른 사용자 조회")
  public ResponseEntity<UserWithProfileImageDto> readOtherInfo(@PathVariable("id") final Long id) {
    final UserWithProfileImageDto user = userFacade.findUserInfo(id);
    return ResponseEntity.ok(user);
  }

  @GetMapping
  @SwaggerOK(summary = "내 정보 조회")
  public ResponseEntity<UserWithProfileImageDto> readMyInfo() {
    final User user = securityUtil.getCurrentUser();
    final UserWithProfileImageDto userWithProfileImageDto = UserWithProfileImageDto.from(user);
    return ResponseEntity.ok(userWithProfileImageDto);
  }

  @GetMapping("v2")
  @SwaggerOK(summary = "내 정보 조회 v2")
  public ResponseEntity<UserWithProfileImageDtoV2> readMyInfoV2() {
    final User user = securityUtil.getCurrentUser();
    final UserWithProfileImageDtoV2 userWithProfileImageDto = UserWithProfileImageDtoV2.from(user, true);
    return ResponseEntity.ok(userWithProfileImageDto);
  }

  @PatchMapping("push")
  @Operation(summary = "푸시 알람 수신 활성화/비활성화", description = "응답 없음")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void changeReceivePermission() {
    final long currentUserId = securityUtil.getCurrentUserId();
    userFacade.toggleReceivePermission(currentUserId);
  }

  @PatchMapping
  @Operation(summary = "사용자 정보 변경", description = "응답 없음")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateUser(@RequestBody @Valid final RequestUserDto requestUserDto) {
    final long currentUserId = securityUtil.getCurrentUserId();
    userFacade.updateUser(requestUserDto, currentUserId);
  }

  @PatchMapping("logout")
  @Operation(summary = "로그아웃", description = "응답 없음")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void logout() {
    final long currentUserId = securityUtil.getCurrentUserId();
    userFacade.logOut(currentUserId);
  }

  @DeleteMapping
  @SwaggerNoContent(summary = "사용자 탈퇴")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser() {
    final long currentUserId = securityUtil.getCurrentUserId();
    userFacade.deleteUser(currentUserId);
  }

  @DeleteMapping("{id}")
  @SwaggerNoContent(summary = "사용자 강제 삭제(개발용)")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void withdrawUser(@PathVariable("id") final long id) {
    userFacade.deleteUser(id);
  }
}
