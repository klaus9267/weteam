package weteam.backend.domain.meeting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import weteam.backend.domain.meeting.dto.meeting.MeetingDto;
import weteam.backend.domain.meeting.entity.Meeting;

import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Optional<Meeting> findByTitle(final String title);

    @Query("""
           SELECT new weteam.backend.domain.meeting.dto.meeting.MeetingDto(m)
           FROM meetings m
                LEFT JOIN FETCH projects p ON m.project.id = p.id
                LEFT JOIN FETCH meeting_users mu ON mu.meeting.id = m.id
                LEFT JOIN FETCH project_users pu ON mu.projectUser.id = pu.id
           WHERE pu.user.id = :userId
           """)
    Page<MeetingDto> findAllByUserId(final Pageable pageable, final Long userId);
}
