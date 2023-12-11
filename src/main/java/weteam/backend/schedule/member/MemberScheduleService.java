package weteam.backend.schedule.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import weteam.backend.config.message.ExceptionMessage;
import weteam.backend.member.MemberService;
import weteam.backend.schedule.member.domain.MemberSchedule;
import weteam.backend.schedule.member.dto.MemberScheduleDto;
import weteam.backend.schedule.member.dto.RequestMemberScheduleDto;
import weteam.backend.schedule.member.repository.MemberScheduleRepository;
import weteam.backend.schedule.member.repository.MemberScheduleRepositorySupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberScheduleService {
    private final MemberScheduleRepository memberScheduleRepository;
    private final MemberScheduleRepositorySupport memberScheduleRepositorySupport;
    private final MemberService memberService;

    public MemberScheduleDto create(RequestMemberScheduleDto scheduleDto, Long memberId) {
        MemberSchedule memberSchedule = memberScheduleRepository.save(scheduleDto.toEntity(memberId));
        return MemberScheduleDto.from(memberSchedule);
    }

    public  List<MemberScheduleDto> findByMonth(int year, int month, Long memberId) {
        LocalDateTime startDate = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endDate = startDate.plusMonths(1).minusMinutes(1);

        List<MemberSchedule> schedules = memberScheduleRepositorySupport.findByMonth(startDate, endDate, memberId);
        return MemberScheduleDto.from(schedules);
    }

    public List<MemberScheduleDto> findByDay(LocalDate date, Long memberId) {
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = startDate.plusDays(1).minusMinutes(1);

        List<MemberSchedule> schedules = memberScheduleRepositorySupport.findByDate(startDate, endDate, memberId);
        return MemberScheduleDto.from(schedules);
    }

    public MemberSchedule loadById(Long id, Long memberId) {
        MemberSchedule memberSchedule = memberScheduleRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND.getMessage()));
        if (!memberSchedule.getMember().getId().equals(memberId)) {
            throw new RuntimeException("다른 사용자의 스케줄");
        }
        return memberSchedule;
    }

    public MemberScheduleDto update(RequestMemberScheduleDto scheduleDto, Long scheduleId, Long memberId) {
        MemberSchedule memberSchedule = this.loadById(scheduleId, memberId);
        return MemberScheduleDto.from(scheduleDto.toEntity(memberId));
    }

    public void updateIsDone(Long id, Long memberId) {
        MemberSchedule memberSchedule = loadById(id, memberId);
        memberSchedule.setDone(!memberSchedule.isDone());
    }

    public void delete(Long id, Long memberId) {
        MemberSchedule memberSchedule = loadById(id, memberId);
        memberScheduleRepository.delete(memberSchedule);
    }
}
