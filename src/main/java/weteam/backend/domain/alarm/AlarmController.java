package weteam.backend.domain.alarm;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.swagger.SwaggerNoContent;
import weteam.backend.application.swagger.SwaggerOK;
import weteam.backend.domain.alarm.dto.AlarmPaginationDto;
import weteam.backend.domain.user.dto.UserDto;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
@Tag(name = "ALARM")
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping
    @SwaggerOK(summary = "알람 조회")
    @PageableAsQueryParam
    public ResponseEntity<AlarmPaginationDto> readAlarms(@AuthenticationPrincipal final UserDto user,@Parameter(hidden = true) Pageable pageable) {
        final AlarmPaginationDto alarmPaginationDto = alarmService.readAlarmList(pageable, user.id());
        return ResponseEntity.ok(alarmPaginationDto);
    }

    @PatchMapping("{alarmId}")
    @SwaggerNoContent(summary = "알람 단건 읽음 처리")
    public void makeAlarmAsRead(
            @PathVariable("alarmId") final Long alarmId,
            @AuthenticationPrincipal final UserDto user
    ) {
        alarmService.makeAlarmAsRead(alarmId, user.id());
    }

    @PatchMapping
    @SwaggerNoContent(summary = "알람 전체 읽음 처리")
    public void makeAllAlarmAsRead(@AuthenticationPrincipal final UserDto user) {
        alarmService.makeAllAlarmAsRead(user.id());
    }
}
