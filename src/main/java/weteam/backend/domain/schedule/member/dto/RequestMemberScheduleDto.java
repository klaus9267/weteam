package weteam.backend.domain.schedule.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;
import weteam.backend.domain.member.domain.Member;
import weteam.backend.domain.schedule.member.MemberSchedule;

import java.time.LocalDateTime;

@Getter
@Builder
public class RequestMemberScheduleDto {
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

    public MemberSchedule toEntity(Long memberId) {
        return MemberSchedule.builder()
                             .title(this.title)
                             .startedAt(this.startedAt)
                             .endedAt(this.endedAt)
                             .place(this.place)
                             .alarm(this.alarm)
                             .repeatType(this.repeatType)
                             .memo(this.memo)
                             .color(this.color)
                             .member(Member.builder().id(memberId).build())
                             .build();
    }
}
