package weteam.backend.domain.schedule.member.dto;

import lombok.Builder;
import weteam.backend.domain.schedule.member.MemberSchedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record MemberScheduleDto(
        Long id,
        String title,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        String place,
        LocalDateTime alarm,
        int repeatType,
        String memo,
        String color) {

    public static MemberScheduleDto from(MemberSchedule memberSchedule) {
        return MemberScheduleDto.builder()
                                .id(memberSchedule.getId())
                                .title(memberSchedule.getTitle())
                                .startedAt(memberSchedule.getStartedAt())
                                .endedAt(memberSchedule.getEndedAt())
                                .place(memberSchedule.getPlace())
                                .alarm(memberSchedule.getAlarm())
                                .repeatType(memberSchedule.getRepeatType())
                                .memo(memberSchedule.getMemo())
                                .color(memberSchedule.getColor())
                                .build();
    }

    public static List<MemberScheduleDto> from(List<MemberSchedule> memberScheduleList) {
        return memberScheduleList.stream()
                                 .map(MemberScheduleDto::from)
                                 .collect(Collectors.toList());
    }
}
