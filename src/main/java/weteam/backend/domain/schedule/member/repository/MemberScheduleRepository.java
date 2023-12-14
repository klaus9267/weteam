package weteam.backend.domain.schedule.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.schedule.member.domain.MemberSchedule;

public interface MemberScheduleRepository extends JpaRepository<MemberSchedule,Long> {
}