package weteam.backend.domain.project.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import weteam.backend.domain.project.entity.Project;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
  @Query("""
      SELECT p
      FROM projects p
      LEFT JOIN FETCH project_users pu ON pu.project.id = p.id
      WHERE p.id = :projectId
           AND pu.user.id = :userId
           AND pu.isEnable = true
      """)
  Optional<Project> findByIdAndUserId(final Long projectId, final Long userId);

  boolean existsByHostId(final Long hostId);

  @Query("""
      SELECT p
      FROM projects p
           LEFT JOIN FETCH project_users pu ON pu.project.id = p.id
      WHERE pu.user.id = :userId
           AND p.isDone = :isDone
           AND pu.isEnable = true
      """)
  Page<Project> findAllByUserIdAndIsDone(final Pageable pageable, final Long userId, final boolean isDone);

  Optional<Project> findByHostIdAndNameAndExplanation(final Long hostId, final String name, final String explanation);


  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Project> findByHashedId(final String hashedId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Project> findByProjectUserListIdIn(final List<Long> projectUserIdList);

  List<Project> findAllByIsDoneAndEndedAtBefore(final boolean isDone, final LocalDate endedAt);

}
