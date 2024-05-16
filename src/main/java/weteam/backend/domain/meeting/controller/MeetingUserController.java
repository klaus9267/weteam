package weteam.backend.domain.meeting.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weteam.backend.domain.common.swagger.SwaggerNoContent;
import weteam.backend.domain.common.swagger.SwaggerOK;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDto;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDtoV2;
import weteam.backend.domain.meeting.service.MeetingUserService;

import java.util.List;

@RestController
@RequestMapping("/api/meeting-users")
@RequiredArgsConstructor
@Tag(name = "MEETING_USER")
public class MeetingUserController {
  private final MeetingUserService meetingUserService;

  @GetMapping("{meetingId}")
  @SwaggerOK(summary = "약속 초대용 hashedId 조회")
  public ResponseEntity<String> readHashedId(@PathVariable("meetingId") final Long meetingId) {
    final String hashedId = meetingUserService.inviteUser(meetingId);
    return ResponseEntity.ok(hashedId);
  }

  @PatchMapping("{hashedId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "약속 초대 수락")
  public void acceptMeeting(@PathVariable("hashedId") final String hashedId) {
    meetingUserService.acceptInvite(hashedId);
  }

  @PatchMapping("{meetingId}/time")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "시간 수정")
  public void updateTimeSlot(
      @PathVariable("meetingId") final Long meetingId,
      @Valid @NotNull @RequestBody final List<RequestTimeSlotDto> timeSlotDtoList
  ) {
    meetingUserService.updateTimeSlot(timeSlotDtoList, meetingId);
  }

  @PatchMapping("{meetingId}/quit")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "약속 나가기(목록 조회시 제외 기능)")
  public void quitMeeting(@PathVariable("meetingId") final Long meetingId) {
    meetingUserService.quitMeeting(meetingId);
  }

  @PatchMapping("v2/{meetingId}/time")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "시간 수정 v2")
  public void updateTimeSlotV2(
      @PathVariable("meetingId") final Long meetingId,
      @Valid @NotNull @RequestBody final RequestTimeSlotDtoV2 timeList
  ) {
    meetingUserService.updateTimeSlotV2(timeList, meetingId);
  }
}
