package weteam.backend.hashtag;

import weteam.backend.hashtag.domain.Hashtag;
import weteam.backend.hashtag.domain.HashtagType;
import weteam.backend.hashtag.domain.MemberHashtag;
import weteam.backend.hashtag.dto.AddHashtagDto;
import weteam.backend.member.domain.Member;

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
