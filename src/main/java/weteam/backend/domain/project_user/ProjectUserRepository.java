package weteam.backend.domain.project_user;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import weteam.backend.domain.project_user.entity.ProjectUser;

import java.util.List;
import java.util.Optional;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<ProjectUser> findByProjectIdAndUserId(final Long projectId, final Long userId);

  @Query("""
      SELECT pu
      FROM project_users pu
          LEFT JOIN FETCH pu.project p
      WHERE p.id = :projectId
          AND pu.isBlack = false
      """)
  List<ProjectUser> findAllByProjectId(final Long projectId);

  @Query("""
      SELECT pu
      FROM project_users pu
          LEFT JOIN FETCH pu.project p
      WHERE pu.id IN :projectUserIdList
          GROUP BY p.id
      HAVING COUNT(DISTINCT pu.id) = :size
      """)
  List<ProjectUser> findAllByIdInWithSameProject(
      @Param("projectUserIdList") List<Long> projectUserIdList,
      @Param("size") long size);
}