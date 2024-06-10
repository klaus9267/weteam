package weteam.backend.common;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.domain.meeting.dto.meeting.CreateMeetingDto;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.project.repository.ProjectRepository;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.System.getenv;

@TestConfiguration
@RequiredArgsConstructor
public class DataInitializer {
  Map<String, String> env = getenv();

  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  private final MeetingRepository meetingRepository;
  public static User testUser;
  List<User> users = new ArrayList<>();
  List<Project> projects = new ArrayList<>();
  List<Meeting> meetings = new ArrayList<>();

  @PostConstruct
  @Transactional
  public void setContext() {
    this.initUsers();
    this.initProjects();
    this.initMeetings();
    testUser = users.get(0);
  }

  private void initUsers() {
    List<User> userList = new ArrayList<>();
    userList.add(User.builder()
        .role(UserRole.USER)
        .receivePermission(true)
        .email("klaus9267@gmail.com")
        .username("kim1")
        .organization("organization")
        .introduction("introduction")
        .uid(env.get("uid"))
        .build()
    );
    for (int i = 0; i < 100; i++) {
      User user = User.builder()
          .username("username" + i)
          .deviceToken("eotadcGTO0ZIvJYQy0Jadp:APA91bGuJy-2TiuCCMEZELje1ym2i2PGl54zEtu2o35K4WjQtuZyJNmyMAdo6hU4B7y5N556C7DO6IIGyvIQ46tI8IJinr-si0ZxYQChZUbZCMbLiGo4d6GQrJ2wYmXrPNSN8Hq0brVm")
          .receivePermission(true)
          .uid("uid" + i)
          .build();
      userList.add(user);
    }
    users = userRepository.saveAll(userList);
  }

  @Transactional
  private void initProjects() {
    List<Project> projectList = new ArrayList<>();
    for (long i = 0; i < 100; i++) {
      Random random = new Random();
      CreateProjectDto projectDto = new CreateProjectDto("name" + i, LocalDate.now(), i, LocalDate.now().plusMonths(1), "explanation" + i);
      Project project = new Project(projectDto, users.get(random.nextInt(2, 100)));
      project.addHashedId("hashedId" + i);
      A:
      for (long j = 0; j < random.nextLong(0, 10); j++) {
        int r = random.nextInt(0, 100);
        List<Long> ids = new ArrayList<>();
        for (ProjectUser projectUser : project.getProjectUserList()) {
          if (projectUser.getUser().getId().equals(users.get(r).getId())) continue A;
          ids.add(projectUser.getUser().getId());
        }

        if (j == 0 && !ids.contains(1L)) project.addProjectUser(users.get(0));
        project.addProjectUser(users.get(r));
      }
      projectList.add(project);
    }

    projects = projectRepository.saveAll(projectList);
  }

  @Transactional
  private void initMeetings() {
    Random random = new Random();
    List<Meeting> list = new ArrayList<>();
    CreateMeetingDto createMeetingDto = new CreateMeetingDto("title", LocalDateTime.now(), 2L, LocalDateTime.now(), null);
    Meeting meeting = Meeting.from(createMeetingDto, users.get(0));
    meeting.addHashedId("test");
    list.add(meeting);

    for (int i = 0; i < 100; i++) {
      CreateMeetingDto meetingDto = new CreateMeetingDto("title" + i, LocalDateTime.now(), 2L, LocalDateTime.now(), null);
      Meeting meeting2 = Meeting.from(meetingDto, users.get(random.nextInt(2, 100)));
      meeting2.addHashedId("test" + i);
      list.add(meeting2);
    }
    meetings = meetingRepository.saveAll(list);
  }
}
