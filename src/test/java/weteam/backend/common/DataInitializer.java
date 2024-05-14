package weteam.backend.common;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.project.repository.ProjectRepository;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@TestConfiguration
@RequiredArgsConstructor
public class DataInitializer {
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  List<User> users = new ArrayList<>();
  List<Project> projects = new ArrayList<>();
  List<Meeting> meetings = new ArrayList<>();

  @PostConstruct
  public void setContext() {
    this.saveUsers();
    this.saveProjects();
  }

  private void saveUsers() {
    List<User> userList = new ArrayList<>();
    userList.add(User.builder().receivePermission(true).username("kim").uid("hIGOWUmXSugwCftVJ2HsF9kiqfh1").build());
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
  private void saveProjects() {
    List<Project> projectList = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      Random random = new Random();
      CreateProjectDto projectDto = new CreateProjectDto("name" + i, LocalDate.now(), i, LocalDate.now().plusMonths(1), "explanation" + 1);
      Project project = new Project(projectDto, users.get(random.nextInt(0, 100)));

      for (long j = 0; j < random.nextLong(0, 10); j++) {
        int r = random.nextInt(0, 100);
        List<Long> ids = new ArrayList<>();
        for (ProjectUser projectUser : project.getProjectUserList()) ids.add(projectUser.getUser().getId());
        if (ids.contains(users.get(r).getId())) continue;

        ProjectUser projectUser = new ProjectUser(null, null, true, users.get(r), project, null);
        project.addProjectUser(projectUser);
      }

      projectList.add(project);
    }
    projects = projectRepository.saveAll(projectList);
  }
}
