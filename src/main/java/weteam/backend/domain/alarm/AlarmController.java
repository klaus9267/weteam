package weteam.backend.domain.alarm;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weteam.backend.domain.alarm.dto.AlarmPaginationDto;
import weteam.backend.domain.common.pagination.param.AlarmPaginationParam;
import weteam.backend.domain.common.swagger.SwaggerNoContent;
import weteam.backend.domain.common.swagger.SwaggerOK;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
@Tag(name = "ALARM")
public class AlarmController {
  private final AlarmService alarmService;
  private final AlarmFacade alarmFacade;

  @GetMapping
  @SwaggerOK(summary = "알람 조회", description = "JOIN = 팀플 참가, EXIT = 팀플 탈퇴, CHANGE_HOST = 호스트 변경, UPDATE_PROJECT = 팀플 수정, KICK = 팀원 강퇴, DONE = 팀플 종료")
  @PageableAsQueryParam
  public ResponseEntity<AlarmPaginationDto> readAlarms(@ParameterObject @Valid final AlarmPaginationParam paginationParam) {
    final AlarmPaginationDto alarmPaginationDto = alarmFacade.pagingAlarms(paginationParam);
    return ResponseEntity.ok(alarmPaginationDto);
  }

  @PatchMapping("{alarmId}")
  @SwaggerNoContent(summary = "알람 단건 읽음 처리")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void makeAlarmAsRead(@PathVariable("alarmId") final long alarmId) {
    alarmFacade.updateAlarmRead(alarmId);
  }

  @PatchMapping
  @SwaggerNoContent(summary = "알람 전체 읽음 처리")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void makeAllAlarmAsRead() {
    alarmFacade.updateAllRead();
  }
}
