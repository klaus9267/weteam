package weteam.backend.domain.hashtag.dto;

import lombok.Builder;
import weteam.backend.domain.hashtag.domain.Hashtag;
import weteam.backend.domain.hashtag.domain.HashtagType;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record HashtagDto(
        Long id,
        String name,
        HashtagType type,
        boolean isUse) {
    public static HashtagDto from(Hashtag hashtag) {
        return HashtagDto.builder()
                         .id(hashtag.getId())
                         .name(hashtag.getName())
                         .type(hashtag.getType())
                         .isUse(hashtag.isUse())
                         .build();
    }

    public static List<HashtagDto> from(List<Hashtag> hashtagList) {
        return hashtagList.stream().map(HashtagDto::from).collect(Collectors.toList());
    }
}
