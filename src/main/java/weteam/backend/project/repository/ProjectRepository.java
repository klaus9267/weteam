package weteam.backend.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.project.domain.Project;

public interface ProjectRepository extends JpaRepository<Project,Long> {

}
