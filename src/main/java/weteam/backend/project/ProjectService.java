package weteam.backend.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.project.domain.ProjectMember;
import weteam.backend.project.domain.Project;
import weteam.backend.project.repository.ProjectMemberRepository;
import weteam.backend.project.repository.ProjectRepository;
import weteam.backend.member.MemberService;
import weteam.backend.member.domain.Member;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberService memberService;

    public void createProject(Long memberId,Project request) {
        Project project = projectRepository.save(request);
        setProjectMember(memberId, project);
    }

    public void setProjectMember(Long memberId, Project project) {
        Member member = memberService.loadById(memberId);
        ProjectMember projectMember = ProjectMember.builder()
                                                   .project(project)
                                                   .member(member)
                                                   .build();
        projectMemberRepository.save(projectMember);
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public Project loadById(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("없는 팀플"));
    }
}
