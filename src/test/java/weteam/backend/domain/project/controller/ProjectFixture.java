package weteam.backend.domain.project.controller;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import weteam.backend.domain.project.ProjectRepository;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project_user.ProjectUserRepository;
import weteam.backend.domain.project_user.entity.ProjectUser;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

import java.util.NoSuchElementException;

@TestComponent
@RequiredArgsConstructor
public class ProjectFixture {
  private final ProjectRepository projectRepository;
  private final ProjectUserRepository projectUserRepository;
  private final UserRepository userRepository;
  private final EntityManager entityManager;

  public  Project joinProject(long userId, long projectId) {
    Project project = projectRepository.findById(projectId).orElseThrow(NoSuchElementException::new);
    User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
    final ProjectUser newProjectUser = ProjectUser.from(project, user);
    projectUserRepository.save(newProjectUser);
    entityManager.refresh(project);

    return project;
  }
}
