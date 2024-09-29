package weteam.backend.domain.meeting_user;

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

import java.util.List;

@RestController
@RequestMapping("/api/meeting-users")
@RequiredArgsConstructor
@Tag(name = "MEETING_USER")
public class MeetingUserController {
  private final MeetingUserService meetingUserService;
  private final MeetingUserFacade meetingUserFacade;

  @GetMapping("{meetingId}")
  @SwaggerOK(summary = "약속 초대용 hashedId 조회")
  public ResponseEntity<String> readHashedId(@PathVariable("meetingId") final Long meetingId) {
    final String hashedId = meetingUserFacade.findMeetingHashedId(meetingId);
    return ResponseEntity.ok(hashedId);
  }

  @PatchMapping("{hashedId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "약속 초대 수락")
  public void acceptMeeting(@PathVariable("hashedId") final String hashedId) {
    meetingUserFacade.acceptInvite(hashedId);
  }

  @PatchMapping("{meetingId}/time")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "시간 수정")
  public void updateTimeSlot(
      @PathVariable("meetingId") final Long meetingId,
      @Valid @NotNull @RequestBody final List<RequestTimeSlotDto> timeSlotDtoList
  ) {
    meetingUserFacade.updateTime(meetingId, timeSlotDtoList);
  }

  @PatchMapping("{meetingId}/displayed")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "약속 목록 조회 시 제외 처리")
  public void updateMeetingDisplayed(@PathVariable("meetingId") final Long meetingId) {
    meetingUserFacade.toggleDisplayed(meetingId);
  }

  @PatchMapping("{meetingId}/quit")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "약속 탈퇴")
  public void quitMeeting(@PathVariable("meetingId") final Long meetingId) {
    meetingUserFacade.quitMeeting(meetingId);
  }
}
