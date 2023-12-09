package weteam.backend.schedule;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weteam.backend.config.dto.Message;
import weteam.backend.schedule.domain.MemberSchedule;
import weteam.backend.schedule.dto.MemberScheduleDto;
import weteam.backend.schedule.mapper.MemberScheduleMapper;
import weteam.backend.schedule.service.MemberScheduleService;

import java.security.Principal;
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
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "개인스케줄 생성")
    public Message<MemberScheduleDto.Res> create(@RequestBody @Valid MemberScheduleDto request, Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        MemberScheduleDto.Res res = memberScheduleService.create(request, memberId);
        return new Message<>(res);
    }

    @GetMapping("/{year}/{month}")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "내 월별 스케줄 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public Message<List<MemberScheduleDto.Res>> findByMonth(@PathVariable("year") int year,
                                                            @PathVariable("month") int month,
                                                            Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        List<MemberSchedule> memberScheduleList = memberScheduleService.findByMonth(year, month, memberId);
        List<MemberScheduleDto.Res> resList = MemberScheduleMapper.instance.toResList(memberScheduleList);
        return new Message<>(resList);
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "내 일별 스케줄 조회")
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    public Message<List<MemberScheduleDto.Res>> findByMonth(@PathVariable("date") LocalDate date,
                                                            Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        List<MemberSchedule> memberScheduleList = memberScheduleService.findByDay(date, memberId);
        List<MemberScheduleDto.Res> resList = MemberScheduleMapper.instance.toResList(memberScheduleList);
        return new Message<>(resList);
    }

//    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('USER')")
//    @Operation(summary = "id에 맞는 스케줄 조회", responses = {
//            @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
//    })
//    public ResponseEntity<MemberScheduleDto.Res> loadById(@PathVariable("id") Long id, Principal principal) {
//        Long memberId = Long.valueOf(principal.getName());
//
//        MemberSchedule memberScheduleList = memberScheduleService.loadById(id, memberId);
//        MemberScheduleDto.Res res = MemberScheduleMapper.instance.toRes(memberScheduleList);
//        return ResponseEntity.ok(res);
//    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "스케줄 수정")
    public void update(@PathVariable("id") Long id,
                       @RequestBody @Valid MemberScheduleDto request,
                       Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        memberScheduleService.update(request, id, memberId);
    }

    @PatchMapping("/{id}/done")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "스케줄 완료 처리")
    public void update(@PathVariable("id") Long id, Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        memberScheduleService.updateIsDone(id, memberId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "스케줄 삭제")
    public void delete(@PathVariable("id") Long id, Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        memberScheduleService.delete(id, memberId);
    }
}
