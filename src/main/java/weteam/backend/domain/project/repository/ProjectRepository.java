package weteam.backend.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.project.entity.Project;

public interface ProjectRepository extends JpaRepository<Project,Long> {

}
