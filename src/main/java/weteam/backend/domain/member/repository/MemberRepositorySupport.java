package weteam.backend.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import weteam.backend.domain.member.entity.Member;

import static weteam.backend.domain.hashtag.domain.QHashtag.hashtag;
import static weteam.backend.domain.hashtag.domain.QMemberHashtag.memberHashtag;
import static weteam.backend.domain.member.entity.QMember.member;


@Repository
public class MemberRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MemberRepositorySupport(JPAQueryFactory queryFactory) {
        super(Member.class);
        this.queryFactory = queryFactory;
    }

    public Member findProfile(Long memberId) {
        return queryFactory.selectFrom(member)
                           .leftJoin(member.memberHashtagList, memberHashtag).fetchJoin()
                           .leftJoin(memberHashtag.hashtag, hashtag).fetchJoin()
                           .where(member.id.eq(memberId),
                                   memberHashtag.isUse.isTrue().or(memberHashtag.isNull()))
                           .distinct()
                           .fetchOne();
    }
}