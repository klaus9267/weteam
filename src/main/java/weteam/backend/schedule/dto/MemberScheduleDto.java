package weteam.backend.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import weteam.backend.schedule.domain.MemberSchedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class MemberScheduleDto {
    @Size(min = 1, max = 50)
    @Schema(description = "개인스케줄 제목", nullable = false, example = "교수님이랑 밥먹기")
    private String title;

    @Schema(description = "시작 날짜", nullable = false, example = "2023.11.23 10:23:32")
    private LocalDateTime startedAt;

    @Schema(description = "시작 날짜", nullable = true, example = "2023.11.23 10:23:32")
    private LocalDateTime endedAt;

    @Schema(description = "장소 ", nullable = true, example = "2공학관 302호")
    private String place;

    @Schema(description = "알람 / null 가능", nullable = true, example = "2023.11.21 11:11:11")
    private LocalDateTime alarm;

    @Schema(description = "반복  ", nullable = true, example = "2023.11.21 11:11:11")
    private int repeatType;

    @Schema(description = "메모 / null 가능", nullable = true, example = "오늘 저녁은 참치김치찌개 jmt 예정")
    private String memo;

    @Schema(description = "스케줄 색상 설정", nullable = true, example = "오렌지 이스 더 뉴 블랙")
    private String color;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Res{
        @Schema(description = "개인 스케줄 id", example = "1")
        private Long id;

        @Schema(description = "개인 스케줄 제목", example = "애국가 4절 외우기")
        private String title;

        @Schema(description = "개인 스케줄 시작날짜", example = "2023-11-23T20:00:00")
        private LocalDateTime startedAt;

        @Schema(description = "개인 스케줄 종료날짜", example = "2023-11-24T20:00:00")
        private LocalDateTime endedAt;

        @Schema(description = "개인 스케줄 장소", example = "연지 스퀘어 지하주차장 b2 C08")
        private String place;

        @Schema(description = "개인 스케줄 알람", example = "2023-11-23T20:00:00")
        private LocalDateTime alarm;

        @Schema(description = "개인 스케줄 반복", example = "뭘로 할까 고민 중 이거 보면 연락ㄱㄱㄱ")
        private int repeatType;

        @Schema(description = "개인 스케줄 메모", example = "겨울 너무 춥다 여름 빨리왔음 좋겠다")
        private String memo;

        @Schema(description = "개인 스케줄 색상값", example = "#ffffff")
        private String color;
    }
}
