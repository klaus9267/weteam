package weteam.backend.domain.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.domain.member.MemberService;
import weteam.backend.domain.member.domain.Member;
import weteam.backend.domain.project.domain.Project;
import weteam.backend.domain.project.domain.ProjectMember;
import weteam.backend.domain.project.dto.ProjectDto;
import weteam.backend.domain.project.mapper.ProjectMapper;
import weteam.backend.domain.project.repository.ProjectMemberRepository;
import weteam.backend.domain.project.repository.ProjectRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberService memberService;
    private final ProjectMemberService projectMemberService;

    public ProjectDto.Res createProject(Long memberId, Project request) {
        Project project = projectRepository.save(request);
        setProjectMember(memberId, project);
        return ProjectMapper.instance.toRes(project);
    }

    public void setProjectMember(Long memberId, Project project) {
        Member member = memberService.findById(memberId);
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

    public void acceptInvite(Long projectId, Long memberId) {
        Project project = loadById(projectId);
        Member member = memberService.findById(memberId);
        projectMemberService.addMember(member, project);
    }
}
