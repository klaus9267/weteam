package weteam.backend.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.ExceptionMessage;
import weteam.backend.application.handler.exception.NotFoundException;
import weteam.backend.domain.member.entity.Member;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findOneById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND));
    }

    public void updateOrganization(Long id, String organization) {
        Member member = this.findOneById(id);
        member.setOrganization(organization);
    }

    public void delete(Long id) {
        Member member = this.findOneById(id);
        memberRepository.delete(member);
    }
}

