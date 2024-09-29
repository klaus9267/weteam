package weteam.backend.domain.meeting.dto.meeting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import weteam.backend.domain.meeting_user.dto.MeetingUserDto;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.project.dto.ProjectDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class MeetingDetailDto {
    private final Long id;

    @Schema(example = "클라이밍 정모", type = "string")
    private final String title;

    private final String hashedId;
    private final Long imageId;

    @Schema(example = "2024-02-05T10:00:00", type = "string")
    private final LocalDateTime startedAt;

    @Schema(example = "2024-02-05T10:00:00", type = "string")
    private final LocalDateTime endedAt;

    private final ProjectDto project;
    private final List<MeetingUserDto> meetingUserList;

    private MeetingDetailDto(final Meeting meeting) {
        this.id = meeting.getId();
        this.title = meeting.getTitle();
        this.hashedId = meeting.getHashedId();
        this.imageId = meeting.getImageId();
        this.startedAt = meeting.getStartedAt();
        this.endedAt = meeting.getEndedAt();
        this.project = meeting.getProject() == null ? null : ProjectDto.from(meeting.getProject());
        this.meetingUserList = MeetingUserDto.from(meeting.getMeetingUserList());
    }

    public static MeetingDetailDto from(final Meeting meeting) {
        return new MeetingDetailDto(meeting);
    }
}
