package weteam.backend.hash_tag.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import weteam.backend.hashtag.domain.Hashtag;
import weteam.backend.hashtag.domain.MemberHashtag;
import weteam.backend.hashtag.dto.HashtagDto;
import weteam.backend.hashtag.mapper.HashtagMapper;
import weteam.backend.member.domain.Member;

@SpringBootTest
class HashtagMapperTest {
    Member member = Member.builder()
                          .uid("testUser")
                          .username("홍길동")
                          .password("1111")
                          .nickname("ssss")
                          .build();
    Hashtag hashtag = Hashtag.builder()
                             .name("이클립스")
                             .type(1)
                             .build();

    @Test
    void toRes() {

        MemberHashtag memberHashtag = MemberHashtag.builder()
                                                   .isUse(false)
                                                   .member(member)
                                                   .hashtag(hashtag)
                                                   .build();
        HashtagDto.Res res = HashtagMapper.instance.toRes(memberHashtag);
        System.out.println(res.toString());
    }
}