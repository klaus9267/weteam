package weteam.backend.domain.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.security.jwt.JwtUtil;
import weteam.backend.domain.auth.dto.JoinDto;

@RestController
@Validated
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "auth API")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final CustomAuthService customAuthService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "응답 없음")
    @ResponseStatus(HttpStatus.CREATED)
    public void join(@RequestBody @Valid JoinDto joinDto) {
        authService.join(joinDto);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<String> login(@RequestBody @Valid JoinDto joinDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(joinDto.uid(), joinDto.password());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return ResponseEntity.ok(jwtUtil.generateToken(authentication));
    }

    @GetMapping("/verify/uid/{uid}")
    @Operation(summary = "아이디 중복확인", description = "응답 없음")
    @ResponseStatus(HttpStatus.OK)
    public void verifyUid(@PathVariable("uid") String uid) {
        authService.verifyUid(uid);
    }

    @GetMapping("/verify/nickname/{nickname}")
    @Operation(summary = "닉네임 중복확인", description = "응답 없음")
    @ResponseStatus(HttpStatus.OK)
    public void verifyNickname(@PathVariable("nickname") String nickname) {
        authService.verifyNickname(nickname);
    }
}
