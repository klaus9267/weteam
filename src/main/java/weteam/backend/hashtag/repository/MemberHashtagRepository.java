package weteam.backend.hashtag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.hashtag.domain.Hashtag;
import weteam.backend.hashtag.domain.MemberHashtag;

import java.util.Optional;

public interface MemberHashtagRepository extends JpaRepository<MemberHashtag, Long> {
    Optional<MemberHashtag> findByHashtag(Hashtag hashtag);
    Optional<MemberHashtag> findByMemberId(Long emberId);

    void deleteAllByMemberId(Long memberId);
}
