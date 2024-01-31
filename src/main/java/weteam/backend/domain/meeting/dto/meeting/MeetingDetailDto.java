package weteam.backend.domain.meeting.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import weteam.backend.domain.project.dto.ProjectDto;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MeetingDetailDto {
    private final Long id;
    private final String title;
    private final LocalDateTime startedAt;
    private final LocalDateTime endedAt;
    private final ProjectDto project;
}
