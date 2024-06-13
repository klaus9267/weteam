package weteam.backend.domain.meeting.entity;

import jakarta.persistence.*;
import lombok.*;
import weteam.backend.application.BaseEntity;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.meeting.dto.meeting.CreateMeetingDto;
import weteam.backend.domain.meeting.dto.meeting.UpdateMeetingDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectUser;
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
  private Long imageId;
  private String title, hashedId;
  private LocalDateTime startedAt, endedAt;

  @Column(columnDefinition = "boolean default false")
  private boolean isDone;

  @ManyToOne(fetch = FetchType.LAZY)
  private User host;

  @ManyToOne(fetch = FetchType.LAZY)
  private Project project;

  @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
  private final List<MeetingUser> meetingUserList = new ArrayList<>();

  private Meeting(final Long meetingId) {
    this.id = meetingId;
  }

  private Meeting(final CreateMeetingDto meetingDto, final User user, final Project project) {
    this.imageId = meetingDto.imageId();
    this.title = meetingDto.title();
    this.startedAt = meetingDto.startedAt();
    this.endedAt = meetingDto.endedAt();
    this.host = user;
    this.project = project;
    for (final ProjectUser projectUser : project.getProjectUserList()) {
      final MeetingUser meetingUser = MeetingUser.from(projectUser.getUser(), this);
      this.meetingUserList.add(meetingUser);
    }
  }

  private Meeting(final CreateMeetingDto meetingDto, final User user) {
    final MeetingUser meetingUser = MeetingUser.from(user, this);

    this.imageId = meetingDto.imageId();
    this.title = meetingDto.title();
    this.startedAt = meetingDto.startedAt();
    this.endedAt = meetingDto.endedAt();
    this.host = user;
    this.meetingUserList.add(meetingUser);
  }

  public static Meeting from(final CreateMeetingDto meetingDto, final User user, final Project project) {
    return new Meeting(meetingDto, user, project);
  }

  public static Meeting from(final CreateMeetingDto meetingDto, final User user) {
    return new Meeting(meetingDto, user);
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

  public void addMeetingUser(final User user) {
    for (final MeetingUser meetingUser : this.meetingUserList) {
      if (meetingUser.getUser().getId().equals(user.getId())) {
        throw new CustomException(ErrorCode.ALREADY_ACCESS_MEETING);
      }
    }
    final MeetingUser meetingUser = MeetingUser.from(user, this);
    this.meetingUserList.add(meetingUser);
  }

  public void done() {
    this.isDone = true;
  }

  public void quit(final Long userId) {
    final Optional<MeetingUser> optionalMeetingUser = this.meetingUserList.stream()
        .filter(meetingUser -> meetingUser.getUser().getId().equals(userId))
        .findFirst();
    optionalMeetingUser.ifPresent(meetingUser -> this.meetingUserList.remove(meetingUser));
  }
}
