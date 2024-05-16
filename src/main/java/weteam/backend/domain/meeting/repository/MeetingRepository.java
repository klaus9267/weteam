package weteam.backend.domain.meeting.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import weteam.backend.domain.meeting.entity.Meeting;

import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
  @Query("""
      SELECT m
      FROM meetings m
         LEFT JOIN FETCH meeting_users mu ON mu.meeting.id = m.id
      WHERE m.id = :meetingId
         AND mu.user.id = :userId
         AND mu.isAccept = true
      """)
  Optional<Meeting> findByIdAndUserId(final Long meetingId, final Long userId);

  @Query("""
      SELECT m
      FROM meetings m
           LEFT JOIN FETCH meeting_users mu ON mu.meeting.id = m.id
      WHERE mu.user.id = :userId
         AND mu.isAccept = true
         AND mu.isDisplayed = true
      """)
  Page<Meeting> findAllByUserId(final Pageable pageable, final Long userId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Meeting> findByHashedId(final String hashedId);
}
