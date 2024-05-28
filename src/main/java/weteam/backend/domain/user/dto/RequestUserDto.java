package weteam.backend.domain.user.dto;

import jakarta.validation.constraints.Size;
import weteam.backend.domain.common.annotation.AtLeastOneNotNull;

@AtLeastOneNotNull
public record RequestUserDto(
    @Size(min = 1, max = 20)
    String username,
    String organization,
    String introduction
) {
}
