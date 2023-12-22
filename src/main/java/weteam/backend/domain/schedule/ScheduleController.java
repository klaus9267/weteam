package weteam.backend.domain.schedule;

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
import weteam.backend.domain.schedule.dto.ScheduleDto;
import weteam.backend.domain.schedule.dto.RequestScheduleDto;

import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/schedules/members")
@RequiredArgsConstructor
@Tag(name = "Member Schedule", description = "jwt 필수")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("")
    @Operation(summary = "개인스케줄 생성")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid RequestScheduleDto scheduleDto) {
        scheduleService.create(scheduleDto, SecurityUtil.getCurrentMemberId());
    }

    @GetMapping("/{year}/{month}")
    @Operation(summary = "내 월별 스케줄 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<List<ScheduleDto>> findByMonth(@PathVariable("year") int year, @PathVariable("month") int month) {
        return new ApiMetaData<>(scheduleService.findByMonth(year, month, SecurityUtil.getCurrentMemberId()));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "내 일별 스케줄 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<List<ScheduleDto>> findByMonth(@PathVariable("date") LocalDate date) {
        return new ApiMetaData<>(scheduleService.findByDay(date, SecurityUtil.getCurrentMemberId()));
    }

    @PatchMapping("/{scheduleId}")
    @Operation(summary = "스케줄 수정")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("scheduleId") Long scheduleId, @RequestBody @Valid RequestScheduleDto requestScheduleDto) {
        scheduleService.update(requestScheduleDto, scheduleId, SecurityUtil.getCurrentMemberId());
    }

    @PatchMapping("/{id}/done")
    @Operation(summary = "스케줄 완료 처리", description = "응답 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Long id) {
        scheduleService.updateIsDone(id, SecurityUtil.getCurrentMemberId());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "스케줄 삭제", description = "응답 없음")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        scheduleService.delete(id, SecurityUtil.getCurrentMemberId());
    }
}
