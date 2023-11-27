package weteam.backend.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.project.domain.ProjectWork;

public interface ProjectWorkRepository extends JpaRepository<ProjectWork,Long> {
}
