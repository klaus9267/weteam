package weteam.backend.domain.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import weteam.backend.domain.project.domain.Project;
import weteam.backend.domain.project.dto.ProjectDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {
    ProjectMapper instance = Mappers.getMapper(ProjectMapper.class);

    Project toEntity(ProjectDto.Create request);

    @Mapping(target = "headCount", source = "headCount")
    ProjectDto.Res toRes(Project project, int headCount);
    ProjectDto.Res toRes(Project project);
}
