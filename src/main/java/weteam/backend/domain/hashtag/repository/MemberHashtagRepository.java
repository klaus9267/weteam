package weteam.backend.domain.hashtag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.hashtag.domain.MemberHashtag;
import weteam.backend.domain.hashtag.domain.Hashtag;

import java.util.Optional;

public interface MemberHashtagRepository extends JpaRepository<MemberHashtag, Long> {
    Optional<MemberHashtag> findByHashtag(Hashtag hashtag);
    Optional<MemberHashtag> findByMemberId(Long memberId);
    void deleteAllByMemberId(Long memberId);
}
