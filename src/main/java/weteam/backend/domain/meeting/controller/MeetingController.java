package weteam.backend.domain.meeting.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weteam.backend.domain.common.pagination.param.MeetingPaginationParam;
import weteam.backend.domain.common.swagger.SwaggerNoContent;
import weteam.backend.domain.common.swagger.SwaggerOK;
import weteam.backend.domain.meeting.dto.meeting.*;
import weteam.backend.domain.meeting.service.MeetingService;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
@Tag(name = "MEETING")
public class MeetingController {
  private final MeetingService meetingService;

  @PostMapping
  @SwaggerOK(summary = "약속 생성")
  public ResponseEntity<MeetingDto> addMeeting(@Valid @RequestBody final CreateMeetingDto meetingDto) {
    final MeetingDto meeting = meetingService.addMeeting(meetingDto);
    return ResponseEntity.ok(meeting);
  }

  @GetMapping
  @SwaggerOK(summary = "약속 목록 조회")
  @PageableAsQueryParam
  public ResponseEntity<MeetingPaginationDto> readMeetingList(@ParameterObject @Valid final MeetingPaginationParam paginationParam) {
    final MeetingPaginationDto meetingDtoList = meetingService.readListWithPagination(paginationParam);
    return ResponseEntity.ok(meetingDtoList);
  }

  @GetMapping("{meetingId}")
  @SwaggerOK(summary = "약속 상세 조회")
  public ResponseEntity<MeetingDetailDto> readMeeting(@PathVariable("meetingId") final Long meetingId) {
    final MeetingDetailDto meetingDetailDto = meetingService.readMeetingDetailDto(meetingId);
    return ResponseEntity.ok(meetingDetailDto);
  }

//  @GetMapping("from-project/{projectId}")
//  @SwaggerOK(summary = "프로젝트에서 약속 목록 조회")
//  public ResponseEntity<MeetingPaginationDto> readMeetingListFromProject(@PathVariable("projectId") final Long projectId) {
//    final MeetingPaginationDto meetingDtoList = meetingService.readListWithPagination(paginationParam);
//    return ResponseEntity.ok(meetingDtoList);
//  }

  @GetMapping("v2/{meetingId}")
  @SwaggerOK(summary = "약속 상세 조회 v2")
  public ResponseEntity<MeetingDetailDtoV2> readMeetingV2(@PathVariable("meetingId") final Long meetingId) {
    final MeetingDetailDtoV2 meetingDetailDtoV2 = meetingService.readMeetingDetailDtoV2(meetingId);
    return ResponseEntity.ok(meetingDetailDtoV2);
  }

  @PatchMapping("{meetingId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "약속 수정")
  public void updateMeeting(
      @PathVariable("meetingId") final Long meetingId,
      @Valid @RequestBody final UpdateMeetingDto meetingDto
  ) {
    meetingService.updateMeeting(meetingDto, meetingId);
  }

  @DeleteMapping("{meetingId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SwaggerNoContent(summary = "약속 삭제")
  public void updateMeeting(@PathVariable("meetingId") final Long meetingId) {
    meetingService.deleteMeeting(meetingId);
  }
}
