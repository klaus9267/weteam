package weteam.backend.domain.hashtag.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import weteam.backend.domain.hashtag.domain.Hashtag;
import weteam.backend.domain.hashtag.domain.HashtagType;

import java.util.List;

import static weteam.backend.domain.hashtag.domain.QHashtag.hashtag;


@Repository
public class HashtagRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public HashtagRepositorySupport(JPAQueryFactory queryFactory) {
        super(Hashtag.class);
        this.queryFactory = queryFactory;
    }

    public List<Hashtag> findByMemberIdWithType(Long memberId, String type) {
        return queryFactory.selectFrom(hashtag)
                           .where(hashtag.member.id.eq(memberId),
                                   hashtag.type.eq(HashtagType.valueOf(type)),
                                   hashtag.isUse.eq(true))
                           .fetch();
    }
}
