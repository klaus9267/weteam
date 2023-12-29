package weteam.backend.domain.member;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import weteam.backend.application.oauth.provider.ProviderType;
import weteam.backend.domain.member.dto.MemberDto;
import weteam.backend.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickname(String nickname);

    @EntityGraph(attributePaths = {"hashtagList"})
    Optional<Member> findOneByProviderAndProviderId(ProviderType provider, String providerId);
}
