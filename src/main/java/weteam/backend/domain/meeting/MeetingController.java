package weteam.backend.domain.meeting;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weteam.backend.domain.common.pagination.param.MeetingPaginationParam;
import weteam.backend.domain.common.swagger.SwaggerNoContent;
import weteam.backend.domain.common.swagger.SwaggerOK;
import weteam.backend.domain.meeting.dto.meeting.*;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
@Tag(name = "MEETING")
@Slf4j
public class MeetingController {
  private final MeetingFacade meetingFacade;

  @PostMapping
  @SwaggerOK(summary = "약속 생성")
  public ResponseEntity<MeetingDto> addMeeting(@Valid @RequestBody final CreateMeetingDto meetingDto) {
    final MeetingDto meeting = meetingFacade.addMeeting(meetingDto);
    return ResponseEntity.ok(meeting);
  }

  @GetMapping
  @SwaggerOK(summary = "약속 목록 조회")
  @PageableAsQueryParam
  public ResponseEntity<MeetingPaginationDto> readMeetingList(@ParameterObject @Valid final MeetingPaginationParam paginationParam) {
    final MeetingPaginationDto meetingDtoList = meetingFacade.pagingMeetings(paginationParam);
    return ResponseEntity.ok(meetingDtoList);
  }

  @GetMapping("{meetingId}")
  @SwaggerOK(summary = "약속 상세 조회")
  public ResponseEntity<MeetingDetailDto> readMeeting(@PathVariable("meetingId") final Long meetingId) {
    final MeetingDetailDto meetingDetailDto = meetingFacade.findMeeting(meetingId);
    return ResponseEntity.ok(meetingDetailDto);
  }

  @PatchMapping("{meetingId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "약속 수정")
  public void updateMeeting(
      @PathVariable("meetingId") final Long meetingId,
      @Valid @RequestBody final UpdateMeetingDto meetingDto
  ) {
    meetingFacade.updateMeeting(meetingId, meetingDto);
  }

  @DeleteMapping("{meetingId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "약속 삭제")
  public void updateMeeting(@PathVariable("meetingId") final Long meetingId) {
    meetingFacade.deleteMeeting(meetingId);
  }
}
