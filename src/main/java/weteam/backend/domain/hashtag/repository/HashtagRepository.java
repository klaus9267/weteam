package weteam.backend.domain.hashtag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.hashtag.domain.Hashtag;
import weteam.backend.domain.hashtag.domain.HashtagType;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findOneByIdAndMemberId(Long hashtagId, Long memberId);
    List<Hashtag> findAllByMemberIdAndType(Long memberId, HashtagType type);
    Optional<Hashtag> findByName(String name);
    Optional<Hashtag> findByMemberId(Long memberId);
    void deleteAllByMemberId(Long memberId);
}
