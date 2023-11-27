package weteam.backend.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.member.domain.Member;
import weteam.backend.member.repository.MemberCustomRepository;
import weteam.backend.member.repository.MemberRepository;

import java.util.List;
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

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Member findMyInfoWithUseHashtag(Long id) {
        return memberCustomRepository.findMyInfoWithUseHashtag(id);
    }

    public Member loadById(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("없는 사용자"));
    }

    public void updateOrganization(Long id, String organization) {
        Member member = loadById(id);
        member.setOrganization(organization);
        memberRepository.save(member);
    }
}

