package weteam.backend.domain.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.handler.exception.NotFoundException;
import weteam.backend.application.message.ExceptionMessage;
import weteam.backend.domain.member.MemberService;
import weteam.backend.domain.member.entity.Member;
import weteam.backend.domain.project.dto.ProjectDto;
import weteam.backend.domain.project.dto.ProjectMemberDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectMember;
import weteam.backend.domain.project.repository.ProjectMemberRepository;
import weteam.backend.domain.project.repository.ProjectRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberService memberService;
    private final ProjectMemberService projectMemberService;

    public ProjectDto create(Long memberId, Project request) {
        Project project = projectRepository.save(request);
        setProjectMember(memberId, project);
        return ProjectDto.from(project, 1);
    }

    public void setProjectMember(Long memberId, Project project) {
        Member member = memberService.findById(memberId);
        ProjectMember projectMember = ProjectMember.builder()
                                                   .project(project)
                                                   .member(member)
                                                   .build();
        projectMemberRepository.save(projectMember);
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND));
    }

    public List<ProjectMemberDto> findMemberListByProject(Long projectId) {
        List<ProjectMember> projectMemberList = projectMemberRepository.findByProjectId(projectId);
        if (projectMemberList.isEmpty()) {
            throw new NotFoundException(ExceptionMessage.NOT_FOUND);
        }
        return ProjectMemberDto.from(projectMemberList);
    }

    public void acceptInvite(Long projectId, Long memberId) {
        Project project = this.findById(projectId);
        Member member = memberService.findById(memberId);
        projectMemberService.addMember(member, project);
    }
}
