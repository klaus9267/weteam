package weteam.backend.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.member.domain.Member;
import weteam.backend.domain.project.domain.Project;
import weteam.backend.domain.project.domain.ProjectMember;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long> {
    Optional<ProjectMember> findByProjectAndMember(Project project, Member member);

    List<ProjectMember> findByProjectId(Long projectId);
}
