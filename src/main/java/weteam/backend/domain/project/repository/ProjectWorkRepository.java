package weteam.backend.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.project.entity.ProjectTask;

public interface ProjectWorkRepository extends JpaRepository<ProjectTask,Long> {
}
