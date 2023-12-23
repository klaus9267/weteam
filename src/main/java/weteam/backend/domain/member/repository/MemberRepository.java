package weteam.backend.domain.member.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.application.oauth.ProviderType;
import weteam.backend.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickname(String nickname);

    @EntityGraph(attributePaths = {"memberHashtagList"})
    Optional<Member> findOneByProviderAndProviderId(ProviderType provider, String providerId);
}
