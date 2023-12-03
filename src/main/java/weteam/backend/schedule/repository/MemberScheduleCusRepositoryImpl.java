package weteam.backend.schedule.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import weteam.backend.schedule.domain.MemberSchedule;

import java.time.LocalDateTime;
import java.util.List;

import static weteam.backend.schedule.domain.QMemberSchedule.memberSchedule;

@Repository
@RequiredArgsConstructor
public class MemberScheduleCusRepositoryImpl implements MemberScheduleCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MemberSchedule> findByMonth(LocalDateTime startDate, LocalDateTime endDate, Long memberId) {
        return jpaQueryFactory.selectFrom(memberSchedule)
                              .where(memberSchedule.startedAt.between(startDate, endDate),
                                     memberSchedule.member.id.eq(memberId))
                              .orderBy(memberSchedule.startedAt.asc())
                              .fetch();
    }

    @Override
    public List<MemberSchedule> findByDate(LocalDateTime startDate, LocalDateTime endDate, Long memberId) {
        return jpaQueryFactory.selectFrom(memberSchedule)
                              .where(memberSchedule.startedAt.between(startDate, endDate),
                                     memberSchedule.member.id.eq(memberId))
                              .orderBy(memberSchedule.startedAt.asc())
                              .fetch();
    }
}
