package weteam.backend.domain.alarm;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weteam.backend.application.oauth.PrincipalDetails;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
@Tag(name = "ALARM")
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping
    @Operation(summary = "알람 조회")
    @PageableAsQueryParam
    public void readAlarms(@AuthenticationPrincipal final PrincipalDetails principalDetails, Pageable pageable) {
        alarmService.readAlarmList(pageable, principalDetails.getUser().id());
    }
}
