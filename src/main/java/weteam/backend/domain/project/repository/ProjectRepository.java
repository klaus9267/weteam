package weteam.backend.domain.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.project.entity.Project;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @EntityGraph(attributePaths = {"projectMemberList", "host"})
    Page<Project> findAllByHostId(Pageable pageable, Long hostId);

    Optional<Project> findByHostIdAndNameAndExplanation(final Long hostId, final String name, final String explanation);
}
