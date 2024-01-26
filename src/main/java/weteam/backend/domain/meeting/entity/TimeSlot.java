package weteam.backend.domain.meeting.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "timeslots")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startedAt, endedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private MeetingUser meetingUser;
}
