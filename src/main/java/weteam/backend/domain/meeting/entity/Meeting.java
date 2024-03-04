package weteam.backend.domain.meeting.entity;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.application.BaseEntity;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
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
  private Integer imageId;
  private String title, hashedId;
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
    final User user = User.from(userId);
    this.imageId = meetingDto.imageId();
    this.title = meetingDto.title();
    this.startedAt = meetingDto.startedAt();
    this.endedAt = meetingDto.endedAt();
    this.host = User.from(userId);
    this.project = project;
    this.meetingUserList = MeetingUser.from(user, this);
  }
  
  private Meeting(final CreateMeetingDto meetingDto, final Long userId) {
    final User user = User.from(userId);
    this.imageId = meetingDto.imageId();
    this.title = meetingDto.title();
    this.startedAt = meetingDto.startedAt();
    this.endedAt = meetingDto.endedAt();
    this.host = user;
    this.meetingUserList = MeetingUser.from(user, this);
  }
  
  public static Meeting from(final CreateMeetingDto meetingDto, final Long userId, final Project project) {
    return new Meeting(meetingDto, userId, project);
  }
  
  public static Meeting from(final CreateMeetingDto meetingDto, final Long userId) {
    return new Meeting(meetingDto, userId);
  }
  
  
  public static Meeting from(final Long meetingId) {
    return new Meeting(meetingId);
  }
  
  public void updateMeeting(final UpdateMeetingDto meetingDto) {
    this.title = meetingDto.title();
    this.startedAt = meetingDto.startedAt();
    this.endedAt = meetingDto.endedAt();
  }
  
  public void addHashedId(final String hashedId) {
    this.hashedId = hashedId;
  }
  
  public void addMeetingUser(final MeetingUser meetingUser) {
    if (this.meetingUserList.contains(meetingUser)) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, "이미 수락한 약속입니다.");
    }
    this.meetingUserList.add(meetingUser);
  }
}
