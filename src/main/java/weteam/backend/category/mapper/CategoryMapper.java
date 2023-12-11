package weteam.backend.category.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import weteam.backend.category.domain.Category;
import weteam.backend.category.dto.CategoryDto;
import weteam.backend.member.domain.Member;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    CategoryMapper instance = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "member",source = "member")
    Category toEntity(CategoryDto request, Member member);

    @Named("E2D")
    CategoryDto.Res toRes(Category category);
    @IterableMapping(qualifiedByName = "E2D")
    List<CategoryDto.Res> toResList(List<Category> categoryList);
}
