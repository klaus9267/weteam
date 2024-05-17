package weteam.backend.domain.meeting.param;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;

@Getter
public class MeetingDetailParam {
  @Parameter(example = "10", required = true)
  private final Long meetingId;
  @Parameter(example = "10")
  private final int minimum;

  public MeetingDetailParam(final Long meetingId, final Integer minimum) {
    this.meetingId = meetingId;
    this.minimum = minimum == null ? 0 : minimum;
  }
}
