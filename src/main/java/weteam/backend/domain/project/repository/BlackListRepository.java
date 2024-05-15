package weteam.backend.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.project.entity.BlackList;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
  boolean existsByUserIdAndProjectId(final Long userId, final Long projectId);
}
