package weteam.backend.hashtag.repository;

import weteam.backend.hashtag.domain.MemberHashtag;

import java.util.List;

public interface MemberHashtagCustomRepository {
    List<MemberHashtag> findByMemberIdWithType(Long memberId, int type);
}
