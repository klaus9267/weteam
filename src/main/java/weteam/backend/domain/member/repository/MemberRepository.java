package weteam.backend.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByNickname(String nickname);
}
