package weteam.backend.domain.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
           AND (pu.user.id = :userId AND pu.project.id = :projectId)
           AND pu.enable = true
      """)
  Optional<Project> findByIdAndUserId(final Long projectId, final Long userId);

  boolean existsByHostId(final Long hostId);

  @Query("""
      SELECT p
      FROM projects p
           LEFT JOIN FETCH project_users pu ON pu.project.id = p.id
      WHERE pu.user.id = :userId
           AND p.done = :done
           AND pu.enable = true
      """)
  Page<Project> findAllByUserIdAndDone(final Pageable pageable, final Long userId, final boolean done);

  Optional<Project> findByHostIdAndNameAndExplanation(final Long hostId, final String name, final String explanation);

  Optional<Project> findByHashedId(final String hashedId);

  Optional<Project> findByProjectUserListIdIn(final List<Long> projectUserIdList);

  List<Project> findAllByDoneAndEndedAtBefore(final boolean done, final LocalDate endedAt);

}
