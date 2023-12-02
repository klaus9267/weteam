package weteam.backend.member.repository;

import weteam.backend.member.domain.Member;

public interface MemberCustomRepository {
    public Member findProfile(Long memberId);
}
