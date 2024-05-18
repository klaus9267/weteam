package weteam.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import weteam.backend.application.BaseEntity;
import weteam.backend.domain.alarm.entity.Alarm;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.entity.MeetingUser;
import weteam.backend.domain.profile.ProfileImage;
import weteam.backend.domain.project.entity.BlackList;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.user.dto.RequestUserDto;
import weteam.backend.domain.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username, organization, uid, email, deviceToken, introduction;

  @Column(columnDefinition = "boolean default false")
  private boolean isLogin;

  private boolean receivePermission;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private ProfileImage profileImage;

  @OneToMany(mappedBy = "host", cascade = CascadeType.ALL)
  private List<Project> projectList = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<ProjectUser> projectUserList = new ArrayList<>();

  @OneToMany(mappedBy = "host", cascade = CascadeType.ALL)
  private List<Meeting> meetingList = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<MeetingUser> meetingUserList = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<BlackList> blackLists = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Alarm> alarms1 = new ArrayList<>();

  @OneToMany(mappedBy = "targetUser", cascade = CascadeType.ALL)
  private List<Alarm> alarms2 = new ArrayList<>();

  public static User from(final UserDto userDto) {
    return User.builder().id(userDto.id())
        .username(userDto.username())
        .email(userDto.email())
        .organization(userDto.organization())
        .role(userDto.role())
        .build();
  }

  public void updateUser(final RequestUserDto userDto) {
    this.organization = userDto.organization();
    this.username = userDto.username();
    this.introduction = userDto.introduction();
  }

  public void updateDevice(final String deviceToken) {
    this.deviceToken = deviceToken;
  }

  public void updateReceivePermission() {
    this.receivePermission = !receivePermission;
  }

  public void logout() {
    this.isLogin = false;
  }

  public void login() {
    this.isLogin = true;
  }
}
