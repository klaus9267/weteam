package weteam.backend.domain.meeting;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weteam.backend.domain.common.pagination.param.MeetingPaginationParam;
import weteam.backend.domain.common.swagger.SwaggerCreated;
import weteam.backend.domain.common.swagger.SwaggerNoContent;
import weteam.backend.domain.common.swagger.SwaggerOK;
import weteam.backend.domain.meeting.dto.RequestTimeSlotDto;
import weteam.backend.domain.meeting.dto.meeting.CreateMeetingDto;
import weteam.backend.domain.meeting.dto.meeting.MeetingDetailDto;
import weteam.backend.domain.meeting.dto.meeting.MeetingPaginationDto;
import weteam.backend.domain.meeting.dto.meeting.UpdateMeetingDto;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
@Tag(name = "MEETING")
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SwaggerCreated(summary = "약속 생성")
    public void addMeeting(@Valid @RequestBody final CreateMeetingDto meetingDto) {
        meetingService.addMeeting(meetingDto);
    }

    @PostMapping("{meetingId}")
    @ResponseStatus(HttpStatus.CREATED)
    @SwaggerCreated(summary = "시간 입력")
    public void addTimeSlot(
            @Valid @NotNull @RequestBody final List<RequestTimeSlotDto> timeSlotDtoList,
            @PathVariable("meetingId") final Long meetingId) {
        meetingService.addTimeSlot(timeSlotDtoList, meetingId);
    }

    @GetMapping
    @SwaggerOK(summary = "약속 목록 조회")
    @PageableAsQueryParam
    public ResponseEntity<MeetingPaginationDto> readMeetingList(@ParameterObject @Valid final MeetingPaginationParam paginationParam) {
        final MeetingPaginationDto meetingDtoList = meetingService.readMeetingList(paginationParam);
        return ResponseEntity.ok(meetingDtoList);
    }

    @GetMapping("{meetingId}")
    @SwaggerOK(summary = "약속 상세 조회")
    public ResponseEntity<MeetingDetailDto> readMeeting(@PathVariable("meetingId") final Long meetingId) {
        final MeetingDetailDto meetingDetailDto = meetingService.readMeeting(meetingId);
        return ResponseEntity.ok(meetingDetailDto);
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

    @PatchMapping("{meetingId}/time")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SwaggerNoContent(summary = "시간 수정")
    public void updateTimeSlot(
            @PathVariable("meetingId") final Long meetingId,
            @Valid @NotNull @RequestBody final List<RequestTimeSlotDto> timeSlotDtoList
    ) {
        meetingService.updateTimeSlot(timeSlotDtoList, meetingId);
    }

    @DeleteMapping("{meetingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SwaggerNoContent(summary = "약속 삭제")
    public void updateMeeting(@PathVariable("meetingId") final Long meetingId) {
        meetingService.deleteMeeting(meetingId);
    }
}
