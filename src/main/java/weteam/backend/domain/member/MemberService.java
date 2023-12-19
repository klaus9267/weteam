package weteam.backend.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import weteam.backend.application.message.ExceptionMessage;
import weteam.backend.domain.member.entity.Member;
import weteam.backend.domain.member.repository.MemberRepository;
import weteam.backend.domain.member.repository.MemberRepositorySupport;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberRepositorySupport memberRepositorySupport;

    public Member create(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public Member findProfile(Long id) {
        Member member = memberRepositorySupport.findProfile(id);
        if (member == null) {
            throw new NotFoundException(ExceptionMessage.NOT_FOUND.getMessage());
        }
        return member;
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND.getMessage()));
    }

    public void updateOrganization(Long id, String organization) {
        Member member = this.findById(id);
        member.setOrganization(organization);
    }
}

