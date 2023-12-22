package weteam.backend.domain.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.handler.exception.NotFoundException;
import weteam.backend.application.message.ExceptionMessage;
import weteam.backend.domain.member.MemberService;
import weteam.backend.domain.schedule.dto.RequestScheduleDto;
import weteam.backend.domain.schedule.dto.ScheduleDto;
import weteam.backend.domain.schedule.repository.ScheduleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
//    private final ScheduleRepositorySupport scheduleRepositorySupport;
    private final MemberService memberService;

    public ScheduleDto create(RequestScheduleDto scheduleDto, Long memberId) {
        Schedule schedule = scheduleRepository.save(scheduleDto.toEntity(memberId));
        return ScheduleDto.from(schedule);
    }

    public  List<ScheduleDto> findByMonth(int year, int month, Long memberId) {
        LocalDateTime startDate = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endDate = startDate.plusMonths(1).minusMinutes(1);

//        List<Schedule> schedules = scheduleRepositorySupport.findByMonth(startDate, endDate, memberId);
//        return ScheduleDto.from(schedules);
        return null;
    }

    public List<ScheduleDto> findByDay(LocalDate date, Long memberId) {
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = startDate.plusDays(1).minusMinutes(1);

//        List<Schedule> schedules = scheduleRepositorySupport.findByDate(startDate, endDate, memberId);
//        return ScheduleDto.from(schedules);
        return null;
    }

    public Schedule loadById(Long id, Long memberId) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND));
        if (!schedule.getMember().getId().equals(memberId)) {
            throw new RuntimeException("다른 사용자의 스케줄");
        }
        return schedule;
    }

    public ScheduleDto update(RequestScheduleDto scheduleDto, Long scheduleId, Long memberId) {
        Schedule schedule = this.loadById(scheduleId, memberId);
        return ScheduleDto.from(scheduleDto.toEntity(memberId));
    }

    public void updateIsDone(Long id, Long memberId) {
        Schedule schedule = loadById(id, memberId);
        schedule.setDone(!schedule.isDone());
    }

    public void delete(Long id, Long memberId) {
        Schedule schedule = loadById(id, memberId);
        scheduleRepository.delete(schedule);
    }
}
