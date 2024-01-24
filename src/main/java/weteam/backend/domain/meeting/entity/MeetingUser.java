package weteam.backend.domain.meeting.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "meeting_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MeetingUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProjectUser projectUser;

    @OneToMany(mappedBy = "meetingUser", cascade = CascadeType.ALL)
    private List<TimeSlot> timeSlotList = new ArrayList<>();
}
