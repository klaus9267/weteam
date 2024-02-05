package weteam.backend.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import weteam.backend.domain.meeting.entity.MeetingUser;

import java.util.Optional;

public interface MeetingUserRepository extends JpaRepository<MeetingUser, Long> {
    @Query("""
           SELECT mu
           FROM meeting_users mu
           WHERE mu.meeting.id = :meetingId
                AND mu.projectUser.user.id = :userId
           """)
    Optional<MeetingUser> findByMeetingIdAndUserId(final Long meetingId, final Long userId);
}
