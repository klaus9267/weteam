package weteam.backend.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class ProjectDto {
    @Getter
    @Builder
    public static class Create{
        @NotBlank
        @Size(min = 1, max = 50)
        private String name;

        @NotNull
        private LocalDate startedAt;
        private LocalDate endedAt;
    }
    @Getter
    @Builder
    public static class Res {
        private Long id;
        private String name;
        private int headCount;
    }
}
