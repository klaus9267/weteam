package weteam.backend.domain.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import weteam.backend.domain.project.entity.Project;

import java.time.LocalDate;

@Data
public class RequestProjectDto {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    private LocalDate startedAt;

    private LocalDate endedAt;

    public Project toEntity() {
        return Project.builder().name(this.name).startedAt(this.startedAt).endedAt(this.endedAt).build();
    }
}
