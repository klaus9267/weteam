package weteam.backend.domain.meeting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import weteam.backend.domain.meeting.dto.meeting.MeetingDto;
import weteam.backend.domain.meeting.entity.Meeting;

import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
  @Query("""
         SELECT m
         FROM meetings m
            LEFT JOIN FETCH meeting_users mu ON mu.meeting.id = m.id
         WHERE m.id = :meetingId
            AND mu.user.id = :userId
            AND mu.accept = true
         """)
  Optional<Meeting> findByIdAndUserId(final Long meetingId, final Long userId);
  
  @Query("""
         SELECT new weteam.backend.domain.meeting.dto.meeting.MeetingDto(m)
         FROM meetings m
              LEFT JOIN FETCH meeting_users mu ON mu.meeting.id = m.id
         WHERE mu.user.id = :userId
            AND mu.accept = true
         """)
  Page<MeetingDto> findAllByUserId(final Pageable pageable, final Long userId);
}
