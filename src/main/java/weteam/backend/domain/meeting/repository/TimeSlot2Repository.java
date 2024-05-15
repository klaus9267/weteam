package weteam.backend.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.meeting.entity.TimeSlot2;

public interface TimeSlot2Repository extends JpaRepository<TimeSlot2, Long> {
  void deleteAllByMeetingUserId(final Long meetingUserId);
}
