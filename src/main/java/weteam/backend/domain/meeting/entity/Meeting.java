package weteam.backend.domain.meeting.entity;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.application.BaseEntity;
import weteam.backend.domain.meeting.dto.meeting.CreateMeetingDto;
import weteam.backend.domain.meeting.dto.meeting.UpdateMeetingDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "meetings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Meeting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime startedAt, endedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User host;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<MeetingUser> meetingUserList = new ArrayList<>();

    private Meeting(final CreateMeetingDto meetingDto, final Long userId,final Project project) {
        this.title = meetingDto.title();
        this.startedAt = meetingDto.startedAt();
        this.endedAt = meetingDto.endedAt();
        this.project = new Project(meetingDto.projectId());
        this.host = new User(userId);
        this.meetingUserList = this.setMeetingUserList(project, this);
    }

    public static Meeting from(final CreateMeetingDto meetingDto, final Long userId, final Project project) {
        return new Meeting(meetingDto, userId, project);
    }

    public void updateMeeting(final UpdateMeetingDto meetingDto) {
        this.title = meetingDto.title();
        this.startedAt = meetingDto.startedAt();
        this.endedAt = meetingDto.endedAt();
    }

    private List<MeetingUser> setMeetingUserList(final Project project, final Meeting meeting) {
        return project.getProjectUserList().stream().map(projectUser -> MeetingUser.from(projectUser, meeting)).toList();
    }
}
