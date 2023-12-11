package weteam.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import weteam.backend.member.domain.Member;

@Getter
public class LoginDto {
    @Size(min = 1, max = 11)
    @NotBlank(message = "uid 누락")
    @Schema(description = "사용자 아이디", nullable = false, example = "test1234")
    private String uid;

    @NotBlank(message = "password 누락")
    @Size(min = 4, max = 50)
    @Schema(description = "사용자 비밀번호", nullable = false, example = "11111111")
    private String password;
}
