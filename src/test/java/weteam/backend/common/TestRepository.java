package weteam.backend.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import weteam.backend.domain.common.HashUtil;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.repository.ProjectRepository;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Random;

@TestConfiguration
public class TestRepository {
  @Autowired
  ProjectRepository projectRepository;
  @Autowired
  UserRepository userRepository;

  public Project saveProject() {
    CreateProjectDto projectDto = new CreateProjectDto("test name", LocalDate.now(), 1L, LocalDate.now(), "test explanation");
    Project project = Project.from(projectDto, DataInitializer.testUser);

    Project savedProject = projectRepository.save(project);
    String hashedId = HashUtil.hashId(savedProject.getId());
    project.addHashedId(hashedId);

    return projectRepository.save(savedProject);
  }

  public User findMe() {
    return userRepository.findByUid("hIGOWUmXSugwCftVJ2HsF9kiqfh1").orElseThrow(NoSuchElementException::new);
  }

  public User findRandomUser() {
    Random random = new Random();
    return userRepository.findById(random.nextLong(2, 100)).orElseThrow(NoSuchElementException::new);
  }
}
