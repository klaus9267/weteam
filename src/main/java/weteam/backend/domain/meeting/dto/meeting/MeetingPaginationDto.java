package weteam.backend.domain.meeting.dto.meeting;

import org.springframework.data.domain.Page;
import weteam.backend.domain.meeting.entity.Meeting;

import java.util.List;

public record MeetingPaginationDto(
    int totalPages,
    int totalElements,
    List<MeetingDto> meetingDtoList
) {
  public static MeetingPaginationDto from(final Page<Meeting> meetingDtoPage) {
    return new MeetingPaginationDto(meetingDtoPage.getTotalPages(), meetingDtoPage.getNumberOfElements(), MeetingDto.from(meetingDtoPage.getContent()));
  }
}
