package weteam.backend.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.project.domain.ProjectMember;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long> {
}
