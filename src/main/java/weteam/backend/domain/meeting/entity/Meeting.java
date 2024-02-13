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
import java.util.Optional;

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
  
  private Meeting(final Long meetingId) {
    this.id = meetingId;
  }
  
  private Meeting(final CreateMeetingDto meetingDto, final Long userId, final Project project) {
    ArrayList<MeetingUser> meetingUsers = new ArrayList<>();
    final User user = User.from(userId);
    final MeetingUser meetingUser = MeetingUser.builder().meeting(this).accept(true).user(user).build();
    meetingUsers.add(meetingUser);
    
    this.title = meetingDto.title();
    this.startedAt = meetingDto.startedAt();
    this.endedAt = meetingDto.endedAt();
    this.project = Project.from(meetingDto.projectId());
    this.host = user;
    this.meetingUserList = meetingUsers;
  }
  
  private Meeting(final CreateMeetingDto meetingDto, final Long userId, final Long projectId) {
    ArrayList<MeetingUser> meetingUsers = new ArrayList<>();
    final User user = User.from(userId);
    final MeetingUser meetingUser = MeetingUser.builder().meeting(this).accept(true).user(user).build();
    meetingUsers.add(meetingUser);
    
    this.title = meetingDto.title();
    this.startedAt = meetingDto.startedAt();
    this.endedAt = meetingDto.endedAt();
    this.host = user;
    this.project = projectId == null ? null : Project.from(projectId);
    this.meetingUserList = meetingUsers;
  }
  
  public static Meeting from(final CreateMeetingDto meetingDto, final Long userId, final Long projectId) {
    return new Meeting(meetingDto, userId, projectId);
  }
  
  
  public static Meeting from(final Long meetingId) {
    return new Meeting(meetingId);
  }
  
  public void updateMeeting(final UpdateMeetingDto meetingDto) {
    this.title = meetingDto.title();
    this.startedAt = meetingDto.startedAt();
    this.endedAt = meetingDto.endedAt();
  }
}
