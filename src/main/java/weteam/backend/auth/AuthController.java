package weteam.backend.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.auth.dto.AuthDto;
import weteam.backend.config.dto.ApiMetaData;
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
    @Operation(summary = "회원가입")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<String> join(@RequestBody @Valid AuthDto.Join request) {
        authService.join(request);
        return new ApiMetaData<>("회원가입 성공");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ResponseEntity<String> login(@RequestBody @Valid AuthDto.Login request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUid(), request.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String jwt = jwtUtil.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/verify/uid/{uid}")
    @Operation(summary = "아이디 중복확인")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<String> verifyUid(@PathVariable("uid") String uid) {
        authService.verifyUid(uid);
        return new ApiMetaData<>("사용 가능한 아이디");
    }

    @GetMapping("/verify/nickname/{nickname}")
    @Operation(summary = "닉네임 중복확인")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<String> verifyNickname(@PathVariable("nickname") String nickname) {
        authService.verifyNickname(nickname);
        return new ApiMetaData<>("사용 가능한 닉네임");
    }
}
