package weteam.backend.domain.alarm;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.oauth.PrincipalDetails;
import weteam.backend.domain.project.dto.RequestProjectDto;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
@Tag(name = "ALARM")
public class AlarmController {
    private final AlarmService alarmService;

//    @PostMapping
//    @Operation(summary = "알람 생성", description = "응답 없음")
//    @ResponseStatus(HttpStatus.CREATED)
//    public void createProject(
//            @RequestBody @Valid RequestProjectDto projectDto,
//            @AuthenticationPrincipal PrincipalDetails principalDetails
//    ) {
//        alarmService.save(principalDetails.getMember().id(), projectDto);
//    }
}
