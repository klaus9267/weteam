package weteam.backend.domain.meeting.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import weteam.backend.domain.meeting.dto.meeting_user.MeetingUserDto;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.project.dto.ProjectDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class MeetingDetailDto {
    private final Long id;
    private final String title;
    private final LocalDateTime startedAt;
    private final LocalDateTime endedAt;
    private final ProjectDto project;
    private final List<MeetingUserDto> meetingUserList;

    private MeetingDetailDto(final Meeting meeting) {
        this.id= meeting.getId();
        this.title=meeting.getTitle();
        this.startedAt=meeting.getStartedAt();
        this.endedAt=meeting.getEndedAt();
        this.project = ProjectDto.from(meeting.getProject());
        this.meetingUserList = MeetingUserDto.from(meeting.getMeetingUserList());
    }

    public static MeetingDetailDto from(final Meeting meeting) {
        return new MeetingDetailDto(meeting);
    }
}
