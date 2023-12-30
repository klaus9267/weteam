package weteam.backend.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.project.entity.ProjectMember;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);

    List<ProjectMember> findByProjectId(Long projectId);
}
