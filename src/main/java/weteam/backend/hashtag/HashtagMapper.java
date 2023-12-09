package weteam.backend.hashtag;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import weteam.backend.hashtag.domain.Hashtag;
import weteam.backend.hashtag.domain.MemberHashtag;
import weteam.backend.hashtag.dto.HashtagDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HashtagMapper {
    HashtagMapper instance = Mappers.getMapper(HashtagMapper.class);


    Hashtag toEntity(HashtagDto request);

    @Named("E2D")
    @Mapping(target = "name", source = "memberHashtag.hashtag.name")
    @Mapping(target = "type", source = "memberHashtag.hashtag.type")
    HashtagDto.Res toRes(MemberHashtag memberHashtag);

    @Named("E2DL")
    @IterableMapping(qualifiedByName = "E2D")
    List<HashtagDto.Res> toResList(List<MemberHashtag> memberHashtagList);

    default String setType(int type) {
        return switch (type) {
            case 1 -> "희망업무";
            case 2 -> "mbti";
            case 3 -> "특기";
            case 4 -> "성격";
            default -> "기타";
        };
    }
}
