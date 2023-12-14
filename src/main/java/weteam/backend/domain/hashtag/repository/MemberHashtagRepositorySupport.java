package weteam.backend.domain.hashtag.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import weteam.backend.domain.hashtag.domain.MemberHashtag;
import weteam.backend.domain.hashtag.domain.HashtagType;

import java.util.List;

import static weteam.backend.hashtag.domain.QMemberHashtag.memberHashtag;


@Repository
public class MemberHashtagRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MemberHashtagRepositorySupport(JPAQueryFactory queryFactory) {
        super(MemberHashtag.class);
        this.queryFactory = queryFactory;
    }

    public List<MemberHashtag> findByMemberIdWithType(Long memberId, String type) {
        return queryFactory.selectFrom(memberHashtag)
                           .where(memberHashtag.member.id.eq(memberId),
                                   memberHashtag.hashtag.type.eq(HashtagType.valueOf(type)),
                                   memberHashtag.isUse.eq(true))
                           .fetch();
    }
}
