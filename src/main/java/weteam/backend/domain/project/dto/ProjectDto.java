package weteam.backend.domain.project.dto;

import lombok.Builder;
import lombok.Data;
import weteam.backend.domain.project.domain.Project;

@Data
@Builder
public class ProjectDto {
        private Long id;
        private String name;
        private int headCount;

        public static ProjectDto from(Project project,int headCount) {
                return new ProjectDto(project.getId(), project.getName(), headCount);
        }
}
