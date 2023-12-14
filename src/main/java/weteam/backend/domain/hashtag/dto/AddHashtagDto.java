package weteam.backend.domain.hashtag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class AddHashtagDto {
    @NotBlank(message = "name 누락")
    @Size(min = 1, max = 11)
    @Schema(description = "해시태그 이름", nullable = false, example = "밤샘인간")
    private String name;

    @Schema(description = "해시태그 타입", nullable = false, example = "mbti")
    private String type;

    @Schema(description = "해시태그 색깔", nullable = false, example = "ffffff")
    private String color;
}
