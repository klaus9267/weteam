package weteam.backend.auth;

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
import weteam.backend.auth.dto.JoinDto;
import weteam.backend.auth.dto.LoginDto;
import weteam.backend.config.dto.ApiMetaData;
import weteam.backend.hashtag.domain.Hashtag;
import weteam.backend.security.jwt.CustomAuthService;
import weteam.backend.security.jwt.JwtUtil;

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
    @ApiResponse(responseCode = "201", useReturnTypeSchema = true)
    public ApiMetaData<?> join(@RequestBody @Valid JoinDto joinDto) {
        authService.join(joinDto);
        return new ApiMetaData<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<String> login(@RequestBody @Valid JoinDto joinDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(joinDto.getUid(), joinDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return ResponseEntity.ok(jwtUtil.generateToken(authentication));
    }

    @GetMapping("/verify/uid/{uid}")
    @Operation(summary = "아이디 중복확인", description = "응답 없음")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<?> verifyUid(@PathVariable("uid") String uid) {
        authService.verifyUid(uid);
        return new ApiMetaData<>(HttpStatus.OK);
    }

    @GetMapping("/verify/nickname/{nickname}")
    @Operation(summary = "닉네임 중복확인", description = "응답 없음")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<?> verifyNickname(@PathVariable("nickname") String nickname) {
        authService.verifyNickname(nickname);
        return new ApiMetaData<>(HttpStatus.OK);
    }
}
