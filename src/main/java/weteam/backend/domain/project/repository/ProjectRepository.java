package weteam.backend.domain.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.project.entity.Project;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @EntityGraph(attributePaths = {"projectUserList", "host"})
    Page<Project> findAllByHostIdAndDone(final Pageable pageable, final Long hostId, final boolean done);

    Optional<Project> findByHostIdAndNameAndExplanation(final Long hostId, final String name, final String explanation);
}
