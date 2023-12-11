package weteam.backend.schedule.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.schedule.member.domain.MemberSchedule;

public interface MemberScheduleRepository extends JpaRepository<MemberSchedule,Long> {
}