package weteam.backend.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import weteam.backend.project.domain.Project;
import weteam.backend.project.dto.ProjectDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {
    ProjectMapper instance = Mappers.getMapper(ProjectMapper.class);

    Project toEntity(ProjectDto.Create request);
}
