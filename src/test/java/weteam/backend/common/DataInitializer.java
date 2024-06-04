package weteam.backend.common;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.entity.BlackList;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.project.repository.BlackListRepository;
import weteam.backend.domain.project.repository.ProjectRepository;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;

import java.time.LocalDate;
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
  private final BlackListRepository blackListRepository;
  public static User testUser;
  List<User> users = new ArrayList<>();
  List<Project> projects = new ArrayList<>();
  List<Meeting> meetings = new ArrayList<>();

  @PostConstruct
  @Transactional
  public void setContext() {
    this.initUsers();
    this.initProjects();
    this.initBlackLists();
    testUser = users.get(0);
  }

  private void initUsers() {
    List<User> userList = new ArrayList<>();
    userList.add(User.builder().role(UserRole.USER).receivePermission(true).username("kim").organization("organization").introduction("introduction").uid(env.get("uid")).build());
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

  private void initProjects() {
    List<Project> projectList = new ArrayList<>();
    for (long i = 0; i < 100; i++) {
      Random random = new Random();
      CreateProjectDto projectDto = new CreateProjectDto("name" + i, LocalDate.now(), i, LocalDate.now().plusMonths(1), "explanation" + 1);
      Project project = new Project(projectDto, users.get(random.nextInt(2, 100)));

      for (long j = 0; j < random.nextLong(0, 10); j++) {
        int r = random.nextInt(0, 100);
        List<Long> ids = new ArrayList<>();
        for (ProjectUser projectUser : project.getProjectUserList()) ids.add(projectUser.getUser().getId());
        if (ids.contains(users.get(r).getId())) continue;
        if (j == 0 && !ids.contains(1L)) project.addProjectUser(users.get(0));
        project.addProjectUser(users.get(r));
      }

      projectList.add(project);
    }

    projects = projectRepository.saveAll(projectList);
  }

  private void initBlackLists() {
    List<BlackList> blackLists = new ArrayList<>();

    A:
    for (Project project : projects) {
      Random random = new Random();
      long n = random.nextLong(0, users.size());

      for (BlackList blackList : project.getBlackLists()) {
        if (blackList.getUser().getId().equals(n)) continue A;
      }
      BlackList blackList = BlackList.from(project, users.get((int) n));
      blackLists.add(blackList);
    }

    blackListRepository.saveAll(blackLists);
  }
}
