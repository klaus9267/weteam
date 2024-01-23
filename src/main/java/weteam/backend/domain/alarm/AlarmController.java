package weteam.backend.domain.alarm;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weteam.backend.application.swagger.SwaggerCreated;
import weteam.backend.domain.alarm.dto.CreateAlarmDto;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
@Tag(name = "ALARM")
public class AlarmController {
    private final AlarmService alarmService;

    //    @GetMapping
    //    @Operation(summary = "알람 조회")
    //    @PageableAsQueryParam
    //    public void readAlarms(@AuthenticationPrincipal final UserDto user, Pageable pageable) {
    //        alarmService.readAlarmList(pageable, user.id());
    //    }

    @PostMapping
    @SwaggerCreated(summary = "알람 생성", description = "응답 없음")
    public void addAlarm(@Valid @RequestBody final CreateAlarmDto alarmDto) {

    }
}
