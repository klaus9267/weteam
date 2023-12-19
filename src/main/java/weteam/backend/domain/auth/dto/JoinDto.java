package weteam.backend.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JoinDto(
        @Size(min = 1, max = 11)
        @NotBlank(message = "uid 누락")
        @Schema(description = "사용자 아이디", nullable = false, example = "test1234")
        String uid,

        @Size(min = 1, max = 11)
        @Schema(description = "사용자 이름", nullable = false, example = "홍유진")
        String username,

        @NotBlank(message = "password 누락")
        @Size(min = 4, max = 50)
        @Schema(description = "사용자 비밀번호", nullable = false, example = "11111111")
        String password,

        @NotBlank(message = "nickname 누락")
        @Size(min = 1, max = 50)
        @Schema(description = "사용자 닉네임", nullable = false, example = "nickname1")
        String nickname) {}
