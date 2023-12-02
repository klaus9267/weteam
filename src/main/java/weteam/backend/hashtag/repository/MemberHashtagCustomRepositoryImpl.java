package weteam.backend.hashtag.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import weteam.backend.hashtag.domain.MemberHashtag;

import java.util.List;

import static weteam.backend.hashtag.domain.QMemberHashtag.memberHashtag;

@Repository
@RequiredArgsConstructor
public class MemberHashtagCustomRepositoryImpl implements MemberHashtagCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MemberHashtag> findByMemberIdWithType(Long memberId, int type) {
        return jpaQueryFactory.selectFrom(memberHashtag)
                              .where(memberHashtag.member.id.eq(memberId),
                                     memberHashtag.hashtag.type.eq(type),
                                     memberHashtag.isUse.eq(true))
                              .fetch();
    }
}
