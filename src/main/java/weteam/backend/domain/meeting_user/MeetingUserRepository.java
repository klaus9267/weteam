package weteam.backend.domain.meeting_user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.meeting_user.entity.MeetingUser;

import java.util.Optional;

public interface MeetingUserRepository extends JpaRepository<MeetingUser, Long> {
  @EntityGraph(attributePaths = "user")
  Optional<MeetingUser> findByMeetingIdAndUserId(final Long meetingId, final Long userId);
}
