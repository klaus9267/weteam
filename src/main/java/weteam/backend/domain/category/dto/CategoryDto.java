package weteam.backend.domain.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CategoryDto {
    @Size(min = 1,max = 8)
    @Schema(description = "카테고리 이름", nullable = false, example = "UI/UX 디자인")
    private String name;

    @NotBlank
    @Schema(description = "색상값", nullable = false, example = "#ffffff")
    private String color;
    @Getter
    @Builder
    public static class Res{
        private Long id;
        private String name;
        private String color;
    }
    public static class ResList{
        List<Res> resList = new ArrayList<>();
    }
}
