package weteam.backend.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.member.domain.Member;
import weteam.backend.member.repository.MemberCustomRepository;
import weteam.backend.member.repository.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberCustomRepository memberCustomRepository;

    public Member create(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public Member findProfile(Long id) {
        return memberCustomRepository.findProfile(id);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new RuntimeException("없는 사용자"));
    }

    public void updateOrganization(Long id, String organization) {
        Member member = findById(id);
        member.setOrganization(organization);
        memberRepository.save(member);
    }
}

