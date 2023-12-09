package weteam.backend.schedule;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.config.dto.ApiMetaData;
import weteam.backend.schedule.dto.MemberScheduleDto;
import weteam.backend.schedule.service.MemberScheduleService;
import weteam.backend.security.SecurityUtil;

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
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<MemberScheduleDto.Res> create(@RequestBody @Valid MemberScheduleDto scheduleDto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return new ApiMetaData<>(memberScheduleService.create(scheduleDto, memberId));
    }

    @GetMapping("/{year}/{month}")
    @Operation(summary = "내 월별 스케줄 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<List<MemberScheduleDto.Res>> findByMonth(@PathVariable("year") int year, @PathVariable("month") int month) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return new ApiMetaData<>(memberScheduleService.findByMonth(year, month, memberId));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "내 일별 스케줄 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public ApiMetaData<List<MemberScheduleDto.Res>> findByMonth(@PathVariable("date") LocalDate date) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return new ApiMetaData<>(memberScheduleService.findByDay(date, memberId));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "스케줄 수정")
    public ApiMetaData<?> update(@PathVariable("id") Long id, @RequestBody @Valid MemberScheduleDto scheduleDto) {
        memberScheduleService.update(scheduleDto, id, SecurityUtil.getCurrentMemberId());
        return new ApiMetaData<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/done")
    @Operation(summary = "스케줄 완료 처리")
    public ApiMetaData<?> update(@PathVariable("id") Long id) {
        memberScheduleService.updateIsDone(id, SecurityUtil.getCurrentMemberId());
        return new ApiMetaData<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "스케줄 삭제")
    public ApiMetaData<?> delete(@PathVariable("id") Long id) {
        memberScheduleService.delete(id, SecurityUtil.getCurrentMemberId());
        return new ApiMetaData<>(HttpStatus.NO_CONTENT);
    }
}
