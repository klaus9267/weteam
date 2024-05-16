package weteam.backend.domain.meeting.dto.time_slot;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record RequestTimeSlotDtoV2(
    @Schema(example = "[\"2024-02-05T10:00:00\", \"2024-02-05T11:00:00\"]", type = "array")
    List<LocalDateTime> timeList
) {
}
