package weteam.backend.schedule;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/schedules/groups")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "Group schedules API / jwt 필수")
public class ProjectScheduleController {
}
