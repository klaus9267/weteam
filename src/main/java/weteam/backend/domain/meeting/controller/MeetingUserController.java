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
import weteam.backend.domain.meeting.service.MeetingUserService;

import java.util.List;

@RestController
@RequestMapping("/api/meeting-users")
@RequiredArgsConstructor
@Tag(name = "MEETING_USER")
public class MeetingUserController {
  private final MeetingUserService meetingUserService;

//  @PostMapping("{meetingId}/{userId}")
//  @ResponseStatus(HttpStatus.NO_CONTENT)
//  @SwaggerNoContent(summary = "약속 초대")
//  public void inviteMeeting(
//      @PathVariable("meetingId") final Long meetingId,
//      @PathVariable("userId") final Long userId
//  ) {
//    meetingUserService.inviteMeeting(meetingId, userId);
//  }

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

  @PatchMapping("{meetingId}/develop")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "약속 초대 수락(개발용)")
  public void acceptInvite4Develop(@PathVariable("meetingId") final Long meetingId) {
    meetingUserService.acceptInvite4Develop(meetingId);
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
}
