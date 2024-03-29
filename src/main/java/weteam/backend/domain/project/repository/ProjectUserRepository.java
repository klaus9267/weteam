package weteam.backend.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.project.entity.ProjectUser;

import java.util.List;
import java.util.Optional;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
  Optional<ProjectUser> findByProjectIdAndUserId(final Long projectId, final Long userId);
  
  List<ProjectUser> findByProjectId(final Long projectId);
}