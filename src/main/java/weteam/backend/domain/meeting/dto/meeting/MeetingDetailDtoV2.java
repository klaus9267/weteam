package weteam.backend.domain.meeting.dto.meeting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import weteam.backend.domain.meeting.dto.meeting_user.MeetingUserDtoV2;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.project.dto.ProjectDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class MeetingDetailDtoV2 {
  private final Long id;

  @Schema(example = "클라이밍 정모", type = "string")
  private final String title;
  private final String hashedId;
  private final Long imageId;
  private final boolean isDone;
  @Schema(example = "2024-02-05T10:00:00", type = "string")
  private final LocalDateTime startedAt;

  @Schema(example = "2024-02-05T10:00:00", type = "string")
  private final LocalDateTime endedAt;

  private final ProjectDto project;
  private final List<MeetingUserDtoV2> meetingUserList;
  private final Map<LocalDateTime, List<String>> timeMap;

  private MeetingDetailDtoV2(final Meeting meeting, Map<LocalDateTime, List<String>> timeMap) {
    this.id = meeting.getId();
    this.title = meeting.getTitle();
    this.hashedId = meeting.getHashedId();
    this.imageId = meeting.getImageId();
    this.isDone = meeting.isDone();
    this.startedAt = meeting.getStartedAt();
    this.endedAt = meeting.getEndedAt();
    this.project = meeting.getProject() == null ? null : ProjectDto.from(meeting.getProject());
    this.meetingUserList = MeetingUserDtoV2.from(meeting.getMeetingUserList());
    this.timeMap = timeMap;
  }

  public static MeetingDetailDtoV2 from(final Meeting meeting, Map<LocalDateTime, List<String>> timeMap) {
    return new MeetingDetailDtoV2(meeting, timeMap);
  }
}
