package weteam.backend.domain.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import weteam.backend.domain.project.entity.Project;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("""
           SELECT p
           FROM projects p
           LEFT JOIN FETCH project_users pu
           WHERE p.id = :projectId AND (pu.user.id = :userId AND pu.project.id = :projectId)
           """)
    Optional<Project> findByIdAndUserId(final Long projectId, final Long userId);

    @EntityGraph(attributePaths = {"projectUserList", "host"})
    Page<Project> findAllByHostIdAndDone(final Pageable pageable, final Long hostId, final boolean done);

    Optional<Project> findByHostIdAndNameAndExplanation(final Long hostId, final String name, final String explanation);
}
