package weteam.backend.domain.meeting.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.profile.dto.ProfileDto;
import weteam.backend.domain.project.dto.ProjectDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class MeetingDto {
    private final Long id;
    private final String title;
    private final LocalDateTime startedAt;
    private final LocalDateTime endedAt;
    private final ProjectDto project;

    private MeetingDto(final Meeting meeting) {
        this.id = meeting.getId();
        this.title = meeting.getTitle();
        this.startedAt = meeting.getStartedAt();
        this.endedAt = meeting.getEndedAt();
        this.project = ProjectDto.from(meeting.getProject());
    }

    public static List<MeetingDto> from(final List <Meeting> meetingList) {
        return meetingList.stream().map(MeetingDto::new).toList();
    }
}
