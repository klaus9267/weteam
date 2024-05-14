package weteam.backend.domain.project.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import weteam.backend.domain.project.entity.ProjectUser;

import java.util.List;
import java.util.Optional;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
  Optional<ProjectUser> findByProjectIdAndUserId(final Long projectId, final Long userId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @EntityGraph(attributePaths = {"user", "user.profileImage"})
  List<ProjectUser> findByProjectId(final Long projectId);
}