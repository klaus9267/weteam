package weteam.backend.domain.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.domain.common.swagger.SwaggerNoContent;
import weteam.backend.domain.common.swagger.SwaggerOK;
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.user.dto.UserWithProfileImageDto;
import weteam.backend.domain.user.entity.User;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "USER")
public class UserController {
  private final UserService userService;
  private final SecurityUtil securityUtil;

  @GetMapping("all")
  @SwaggerOK(summary = "모든 사용자 조회(개발용)")
  public ResponseEntity<List<UserDto>> readAll() {
    final List<UserDto> userDtoList = userService.findAll();
    return ResponseEntity.ok(userDtoList);
  }

  @GetMapping("{id}")
  @SwaggerOK(summary = "다른 사용자 조회")
  public ResponseEntity<UserWithProfileImageDto> readOne(@PathVariable("id") final Long id) {
    final UserWithProfileImageDto user = userService.findOneById(id);
    return ResponseEntity.ok(user);
  }

  @GetMapping
  @SwaggerOK(summary = "내 정보 조회")
  public ResponseEntity<UserWithProfileImageDto> readMyInfo() {
    final User user = securityUtil.getCurrentUser();
    return ResponseEntity.ok(UserWithProfileImageDto.from(user));
  }

  @PatchMapping("{organization}")
  @Operation(summary = "사용자 소속 변경", description = "응답 없음")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void changeOrganization(@PathVariable("organization") final String organization) {
    userService.updateOne(organization);
  }

  @DeleteMapping
  @SwaggerNoContent(summary = "사용자 탈퇴", description = "이거는 토큰 없어도 됨 :)")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser() {
    userService.deleteOne();
  }

  @DeleteMapping("all")
  @SwaggerNoContent(summary = "사용자 전체 삭제(개발용)", description = "이거는 토큰 없어도 됨 :)")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAll() {
    userService.deleteAll();
  }
}
