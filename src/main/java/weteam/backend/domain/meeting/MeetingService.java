package weteam.backend.domain.meeting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.domain.meeting.repository.MeetingRepository;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final SecurityUtil securityUtil;
}
