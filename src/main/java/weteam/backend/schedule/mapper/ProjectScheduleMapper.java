package weteam.backend.schedule.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectScheduleMapper {
    ProjectScheduleMapper instance = Mappers.getMapper(ProjectScheduleMapper.class);


}
