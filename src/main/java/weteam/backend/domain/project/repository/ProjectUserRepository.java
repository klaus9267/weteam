package weteam.backend.domain.project.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import weteam.backend.domain.project.entity.ProjectUser;

import java.util.List;
import java.util.Optional;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<ProjectUser> findByProjectIdAndUserId(final Long projectId, final Long userId);

  @Query("""
      SELECT pu
      FROM project_users pu
          LEFT JOIN FETCH pu.project p
          LEFT JOIN FETCH p.blackLists b
      WHERE p.id = :projectId
        AND pu.isEnable = true
        AND pu.user.id NOT IN (
            SELECT bl.user.id
            FROM black_list bl
            WHERE bl.project.id = :projectId
        )
      """)
  List<ProjectUser> findAllByProjectIdWhereNotBlackList(final Long projectId);
}