package weteam.backend.domain.hashtag;

import weteam.backend.domain.hashtag.domain.MemberHashtag;
import weteam.backend.domain.member.entity.Member;
import weteam.backend.domain.hashtag.domain.Hashtag;
import weteam.backend.domain.hashtag.domain.HashtagType;
import weteam.backend.domain.hashtag.dto.AddHashtagDto;

public class HashtagMapper {
    public static Hashtag toHashtag(AddHashtagDto hashtagDto) {
        return Hashtag.builder().name(hashtagDto.getName()).type(HashtagType.valueOf(hashtagDto.getType())).build();
    }

    public static MemberHashtag toMemberHashtag(AddHashtagDto hashtagDto, Member member) {
        return MemberHashtag.builder().color(hashtagDto.getColor()).member(member).hashtag(toHashtag(hashtagDto)).build();
    }

    public static MemberHashtag toMemberHashtag(Hashtag hashtag, Member member, String color) {
        return MemberHashtag.builder().color(color).member(member).hashtag(hashtag).build();
    }
}
