package weteam.backend.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.member.MemberService;
import weteam.backend.member.domain.Member;
import weteam.backend.schedule.domain.MemberSchedule;
import weteam.backend.schedule.dto.MemberScheduleDto;
import weteam.backend.schedule.mapper.MemberScheduleMapper;
import weteam.backend.schedule.repository.MemberScheduleCustomRepository;
import weteam.backend.schedule.repository.MemberScheduleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberScheduleService {
    private final MemberScheduleRepository memberScheduleRepository;
    private final MemberScheduleCustomRepository memberScheduleCustomRepository;
    private final MemberService memberService;

    public void create(MemberScheduleDto request, Long memberId) {
        Member member = memberService.findById(memberId);
        MemberSchedule memberSchedule = MemberScheduleMapper.instance.toEntity(request, member);

        memberScheduleRepository.save(memberSchedule);
    }

    public  List<MemberSchedule> findByMonth(int year, int month, Long memberId) {
        Member member = memberService.findById(memberId);

        LocalDateTime startDate = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endDate = startDate.plusMonths(1).minusMinutes(1);

        return memberScheduleCustomRepository.findByMonth(startDate, endDate, memberId);
    }

    public List<MemberSchedule> findByDay(LocalDate date, Long memberId) {
        Member member = memberService.findById(memberId);

        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = startDate.plusDays(1).minusMinutes(1);

        return memberScheduleCustomRepository.findByDate(startDate, endDate, memberId);
    }

    public Optional<MemberSchedule> findById(Long id) {
        return memberScheduleRepository.findById(id);
    }

    public MemberSchedule loadById(Long id, Long memberId) {
        MemberSchedule memberSchedule = findById(id).orElseThrow(() -> new RuntimeException("없는 스케줄"));
        if (!memberSchedule.getMember().getId().equals(memberId)) {
            throw new RuntimeException("다른 사용자의 스케줄");
        }

        return memberSchedule;
    }

    public void update(MemberScheduleDto request, Long id, Long memberId) {
        Member member = memberService.findById(memberId);
        MemberSchedule memberSchedule = MemberScheduleMapper.instance.toEntity(request, member);

        if (!memberSchedule.getMember().getId().equals(memberId)) {
            throw new RuntimeException("다른 사용자의 스케줄");
        }

        memberScheduleRepository.save(memberSchedule);
    }

    public void updateIsDone(Long id, Long memberId) {
        MemberSchedule memberSchedule = loadById(id, memberId);

        memberSchedule.setDone(!memberSchedule.isDone());
        memberScheduleRepository.save(memberSchedule);
    }

    public void delete(Long id, Long memberId) {
        MemberSchedule memberSchedule = loadById(id, memberId);
        memberScheduleRepository.delete(memberSchedule);
    }
}
