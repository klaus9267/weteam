package weteam.backend.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.member.domain.Member;
import weteam.backend.project.domain.Project;
import weteam.backend.project.domain.ProjectMember;
import weteam.backend.project.repository.ProjectMemberRepository;
import weteam.backend.project.repository.ProjectRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;

    public void addMember(Member member, Project project) {
        if (projectMemberRepository.findByProjectAndMember(project, member).isPresent()) {
            throw new RuntimeException("이미 초대된 사용자");
        }
        ProjectMember projectMember = ProjectMember.builder()
                                                   .member(member)
                                                   .project(project)
                                                   .build();
        projectMemberRepository.save(projectMember);
    }
}
