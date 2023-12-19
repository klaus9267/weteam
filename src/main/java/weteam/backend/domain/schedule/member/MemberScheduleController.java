package weteam.backend.domain.schedule.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.application.common.ApiMetaData;
import weteam.backend.application.security.SecurityUtil;
import weteam.backend.domain.schedule.member.dto.MemberScheduleDto;
import weteam.backend.domain.schedule.member.dto.RequestMemberScheduleDto;

import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/schedules/members")
@RequiredArgsConstructor
@Tag(name = "Member Schedule", description = "jwt 필수")
public class MemberScheduleController {
    private final MemberScheduleService memberScheduleService;

    @PostMapping("")
    @Operation(summary = "개인스케줄 생성")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid RequestMemberScheduleDto scheduleDto) {
        memberScheduleService.create(scheduleDto, SecurityUtil.getCurrentMemberId());
    }

    @GetMapping("/{year}/{month}")
    @Operation(summary = "내 월별 스케줄 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<List<MemberScheduleDto>> findByMonth(@PathVariable("year") int year, @PathVariable("month") int month) {
        return new ApiMetaData<>(memberScheduleService.findByMonth(year, month, SecurityUtil.getCurrentMemberId()));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "내 일별 스케줄 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<List<MemberScheduleDto>> findByMonth(@PathVariable("date") LocalDate date) {
        return new ApiMetaData<>(memberScheduleService.findByDay(date, SecurityUtil.getCurrentMemberId()));
    }

    @PatchMapping("/{scheduleId}")
    @Operation(summary = "스케줄 수정")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("scheduleId") Long scheduleId, @RequestBody @Valid RequestMemberScheduleDto requestMemberScheduleDto) {
        memberScheduleService.update(requestMemberScheduleDto, scheduleId, SecurityUtil.getCurrentMemberId());
    }

    @PatchMapping("/{id}/done")
    @Operation(summary = "스케줄 완료 처리", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Long id) {
        memberScheduleService.updateIsDone(id, SecurityUtil.getCurrentMemberId());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "스케줄 삭제", description = "응답 없음")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        memberScheduleService.delete(id, SecurityUtil.getCurrentMemberId());
    }
}
