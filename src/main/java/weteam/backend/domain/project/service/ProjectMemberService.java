package weteam.backend.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.repository.ProjectMemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;

    public void addMember(Long memberId, Project project) {

    }
}
