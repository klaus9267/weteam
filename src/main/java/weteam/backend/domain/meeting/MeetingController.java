package weteam.backend.domain.meeting;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
@Tag(name = "MEETING", description = "언제보까 | 웬투밋")
public class MeetingController {
    private final MeetingService meetingService;
}
