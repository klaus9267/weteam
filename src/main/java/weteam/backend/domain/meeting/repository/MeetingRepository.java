package weteam.backend.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.meeting.entity.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
