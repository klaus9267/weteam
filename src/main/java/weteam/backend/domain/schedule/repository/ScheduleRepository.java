package weteam.backend.domain.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.schedule.Schedule;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
//    Optional<Schedule> findByIdAnd
}