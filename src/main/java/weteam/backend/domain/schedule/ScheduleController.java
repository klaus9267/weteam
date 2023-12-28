//package weteam.backend.domain.schedule;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import weteam.backend.application.common.ApiMetaData;
//import weteam.backend.application.oauth.PrincipalDetails;
//import weteam.backend.application.security.SecurityUtil;
//import weteam.backend.domain.schedule.dto.ScheduleDto;
//import weteam.backend.domain.schedule.dto.RequestScheduleDto;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@RestController
//@Validated
//@RequestMapping("/api/schedules")
//@RequiredArgsConstructor
//@Tag(name = "Schedule")
//public class ScheduleController {
//    private final ScheduleService scheduleService;
//
//    @PostMapping("")
//    @Operation(summary = "개인스케줄 생성")
//    @ResponseStatus(HttpStatus.CREATED)
//    public void create(
//            @RequestBody @Valid RequestScheduleDto scheduleDto,
//            @AuthenticationPrincipal PrincipalDetails principalDetails
//    ) {
//        scheduleService.create(scheduleDto, principalDetails.getMember().id());
//    }
//
//    @GetMapping("/{year}/{month}")
//    @Operation(summary = "내 월별 스케줄 조회")
//    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
//    public ApiMetaData<List<ScheduleDto>> findByMonth(
//            @PathVariable("year") int year,
//            @PathVariable("month") int month,
//            @AuthenticationPrincipal PrincipalDetails principalDetails
//    ) {
//        return new ApiMetaData<>(scheduleService.findByMonth(year, month, principalDetails.getMember().id()));
//    }
//
//    @GetMapping("/date/{date}")
//    @Operation(summary = "내 일별 스케줄 조회")
//    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
//    public ApiMetaData<List<ScheduleDto>> findByMonth(
//            @PathVariable("date") LocalDate date,
//            @AuthenticationPrincipal PrincipalDetails principalDetails
//    ) {
//        return new ApiMetaData<>(scheduleService.findByDay(date, principalDetails.getMember().id()));
//    }
//
//    @PatchMapping("/{scheduleId}")
//    @Operation(summary = "스케줄 수정")
//    @ResponseStatus(HttpStatus.OK)
//    public void update(
//            @PathVariable("scheduleId") Long scheduleId,
//            @RequestBody @Valid RequestScheduleDto requestScheduleDto,
//            @AuthenticationPrincipal PrincipalDetails principalDetails
//    ) {
//        scheduleService.update(requestScheduleDto, scheduleId, principalDetails.getMember().id());
//    }
//
//    @PatchMapping("/{id}/done")
//    @Operation(summary = "스케줄 완료 처리", description = "응답 없음")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void update(
//            @PathVariable("id") Long id,
//            @AuthenticationPrincipal PrincipalDetails principalDetails
//    ) {
//        scheduleService.updateIsDone(id, principalDetails.getMember().id());
//    }
//
//    @DeleteMapping("/{id}")
//    @Operation(summary = "스케줄 삭제", description = "응답 없음")
//    @ResponseStatus(HttpStatus.OK)
//    public void delete(
//            @PathVariable("id") Long id,
//            @AuthenticationPrincipal PrincipalDetails principalDetails
//    ) {
//        scheduleService.delete(id, principalDetails.getMember().id());
//    }
//}
