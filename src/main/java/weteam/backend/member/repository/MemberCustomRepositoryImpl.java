package weteam.backend.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import weteam.backend.member.domain.Member;

import static weteam.backend.hash_tag.domain.QHashtag.hashtag;
import static weteam.backend.hash_tag.domain.QMemberHashtag.memberHashtag;
import static weteam.backend.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Member findMyInfoWithUseHashtag(Long memberId) {
        return jpaQueryFactory.selectFrom(member)
                                     .leftJoin(member.memberHashtagList, memberHashtag).fetchJoin()
                                     .leftJoin(memberHashtag.hashtag, hashtag).fetchJoin()
                                     .where(member.id.eq(memberId),
                                            memberHashtag.isUse.isTrue().or(memberHashtag.isNull()))
                                     .distinct()
                                     .fetchOne();
    }
}