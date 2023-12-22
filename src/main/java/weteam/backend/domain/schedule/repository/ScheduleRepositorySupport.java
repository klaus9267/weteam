//package weteam.backend.domain.schedule.repository;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
//import org.springframework.stereotype.Repository;
//import weteam.backend.domain.schedule.Schedule;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//
//@Repository
//public class ScheduleRepositorySupport extends QuerydslRepositorySupport {
//    private final JPAQueryFactory queryFactory;
//
//    public ScheduleRepositorySupport(JPAQueryFactory queryFactory) {
//        super(Schedule.class);
//        this.queryFactory = queryFactory;
//    }
//
//    public List<Schedule> findByMonth(LocalDateTime startDate, LocalDateTime endDate, Long memberId) {
//        return queryFactory.selectFrom(QMemberSchedule.memberSchedule)
//                           .where(QMemberSchedule.memberSchedule.startedAt.between(startDate, endDate),
//                                   QMemberSchedule.memberSchedule.member.id.eq(memberId))
//                           .orderBy(QMemberSchedule.memberSchedule.startedAt.asc())
//                           .fetch();
//    }
//
//    public List<Schedule> findByDate(LocalDateTime startDate, LocalDateTime endDate, Long memberId) {
//        return queryFactory.selectFrom(QMemberSchedule.memberSchedule)
//                           .where(QMemberSchedule.memberSchedule.startedAt.between(startDate, endDate),
//                                   QMemberSchedule.memberSchedule.member.id.eq(memberId))
//                           .orderBy(QMemberSchedule.memberSchedule.startedAt.asc())
//                           .fetch();
//    }
//}
