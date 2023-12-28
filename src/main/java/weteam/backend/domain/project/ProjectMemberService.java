package weteam.backend.domain.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.ExceptionMessage;
import weteam.backend.application.handler.exception.DuplicateKeyException;
import weteam.backend.domain.member.entity.Member;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectMember;
import weteam.backend.domain.project.repository.ProjectMemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;

    public void addMember(Long memberId, Project project) {

    }
}
