package weteam.backend.domain.hashtag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import weteam.backend.domain.hashtag.domain.HashtagType;


public record AddHashtagDto(
        @NotBlank(message = "name 누락")
        @Size(min = 1, max = 11)
        String name,
        @Schema(description = "해시태그 타입", nullable = true, example = "PERSONALITY")
        HashtagType type,
        @Schema(description = "해시태그 색깔", nullable = true, example = "ffffff")
        String color) {}
