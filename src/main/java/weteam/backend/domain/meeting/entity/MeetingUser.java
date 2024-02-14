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
  
  @Column(columnDefinition = "boolean default false")
  private boolean accept;
  
  @ManyToOne(fetch = FetchType.LAZY)
  private Meeting meeting;
  
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;
  
  @OneToMany(mappedBy = "meetingUser", cascade = CascadeType.ALL)
  private List<TimeSlot> timeSlotList = new ArrayList<>();
  
  private MeetingUser(final User user, final Meeting meeting) {
    this.meeting = meeting;
    this.user = user;
  }
  
  private MeetingUser(final ProjectUser projectUser, final Meeting meeting) {
    this.user = projectUser.getUser();
    this.accept = false;
    this.meeting = meeting;
  }
  
  public static List<MeetingUser> from(final User user, final Meeting meeting) {
    return List.of(new MeetingUser(user, meeting));
  }
  
  public static MeetingUser from(final Long userId, final Long meetingId) {
    return new MeetingUser(User.from(userId), Meeting.from(meetingId));
  }
  
  public static List<MeetingUser> from(final List<ProjectUser> projectUserList, final Meeting meeting) {
    return projectUserList.stream().map(projectUser -> new MeetingUser(projectUser, meeting)).toList();
  }
  
  public void acceptMeeting() {
    this.accept = !isAccept();
  }
}
