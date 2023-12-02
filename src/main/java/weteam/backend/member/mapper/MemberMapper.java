package weteam.backend.member.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import weteam.backend.auth.dto.AuthDto;
import weteam.backend.hashtag.domain.MemberHashtag;
import weteam.backend.hashtag.dto.HashtagDto;
import weteam.backend.member.domain.Member;
import weteam.backend.member.dto.MemberDto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    MemberMapper instance = Mappers.getMapper(MemberMapper.class);

    Member extractMember(AuthDto.Join request);

    @Mapping(target = "hashtagList", source = "memberHashtagList", qualifiedByName = "setHashtagList")
    MemberDto.Res toRes(Member member);
//    @Mapping(target = "image", ignore = true)
//    Member toEntity(MemberDto.Join request);
//
//    @Mapping(target = "imageUrl", ignore = true)
//    MemberDto.LoginRes toLoginRes(Member member);
//
//    @Mapping(target = "jwt", source = "jwt")
//    @Mapping(target = "imageUrl", ignore = true)
//    @Mapping(target = "hashtagList", source = "member.memberHashtagList", qualifiedByName = "setHashtagList")
//    MemberDto.LoginRes toLoginRes(Member member, String jwt);
//
//    @Mapping(target = "jwt", source = "jwt")
//    MemberDto.JoinRes toJoinRes(Member member, String jwt);
//
    @Named("setHashtagList")
    default List<HashtagDto.Res> setHashtagList(List<MemberHashtag> hashtagList) {
        return hashtagList.stream().map(memberHashtag -> {
            return HashtagDto.Res.builder()
                                 .id(memberHashtag.getId())
                                 .name(memberHashtag.getHashtag().getName())
                                 .type(memberHashtag.getHashtag().getType())
                                 .isUse(memberHashtag.isUse())
                                 .build();
        }).collect(Collectors.toList());
    }
}
