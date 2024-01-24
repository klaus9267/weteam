package weteam.backend.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.meeting.entity.MeetingUser;

public interface MeetingUserRepository extends JpaRepository<MeetingUser, Long> {
}
