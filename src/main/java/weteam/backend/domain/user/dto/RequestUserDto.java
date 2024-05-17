package weteam.backend.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestUserDto(
    @Size(min = 1, max = 20)
    @NotBlank(message = "username is required")
    String username,
    @NotBlank(message = "organization is required")
    String organization,
    @NotBlank(message = "introduction is required")
    String introduction
) {
}
