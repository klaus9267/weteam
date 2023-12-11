package weteam.backend.hashtag.dto;

import lombok.Builder;
import weteam.backend.hashtag.domain.MemberHashtag;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record HashtagDto(
        Long id,
        String name,
        String type,
        boolean isUse) {
    public static HashtagDto from(MemberHashtag hashtag) {
        return HashtagDto.builder()
                         .id(hashtag.getId())
                         .name(hashtag.getHashtag().getName())
                         .type(hashtag.getHashtag().getType().getTitle())
                         .isUse(hashtag.isUse())
                         .build();
    }

    public static List<HashtagDto> from(List<MemberHashtag> hashtagList) {
        return hashtagList.stream().map(HashtagDto::from).collect(Collectors.toList());
    }
}
