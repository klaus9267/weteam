package weteam.backend.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.member.domain.Member;
import weteam.backend.project.domain.Project;
import weteam.backend.project.domain.ProjectMember;

import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long> {
    Optional<ProjectMember> findByProjectAndMember(Project project, Member member);
}
