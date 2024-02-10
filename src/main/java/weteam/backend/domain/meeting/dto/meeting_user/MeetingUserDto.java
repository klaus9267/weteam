package weteam.backend.domain.meeting.dto.meeting_user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import weteam.backend.domain.meeting.dto.time_slot.TimeSlotDto;
import weteam.backend.domain.meeting.entity.MeetingUser;
import weteam.backend.domain.project.dto.ProjectUserDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class MeetingUserDto {
    private final Long id;
    private final ProjectUserDto projectUser;
    private final List<TimeSlotDto> timeSlotList;

    private MeetingUserDto(final MeetingUser meetingUser) {
        this.id = meetingUser.getId();
        this.projectUser = ProjectUserDto.from(meetingUser.getProjectUser());
        this.timeSlotList = TimeSlotDto.from(meetingUser.getTimeSlotList());
    }

    public static List<MeetingUserDto> from(final List<MeetingUser> meetingUser) {
        return meetingUser.stream().map(MeetingUserDto::new).collect(Collectors.toList());
    }
}
