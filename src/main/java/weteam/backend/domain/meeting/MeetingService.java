package weteam.backend.domain.meeting;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.CustomErrorCode;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.common.pagination.param.MeetingPaginationParam;
import weteam.backend.domain.meeting.dto.meeting.*;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final ProjectRepository projectRepository;
    private final SecurityUtil securityUtil;

    public MeetingDetailDto readMeeting(final Long meetingId) {
        final Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_MEETING));
        return MeetingDetailDto.from(meeting);
    }

    public MeetingPaginationDto readMeetingList(final MeetingPaginationParam paginationParam) {
        final Page<MeetingDto> meetingPage = meetingRepository.findAllByUserId(paginationParam.toPageable(), securityUtil.getId());
        return MeetingPaginationDto.from(meetingPage);
    }

    @Transactional
    public void addMeeting(final CreateMeetingDto meetingDto) {
        final Project project = projectRepository.findById(meetingDto.projectId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PROJECT));
        final Meeting meeting = Meeting.from(meetingDto, securityUtil.getId(), project);
        meetingRepository.save(meeting);
    }

    @Transactional
    public void updateMeeting(final UpdateMeetingDto meetingDto, final Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_MEETING));
        meeting.updateMeeting(meetingDto);
    }

    @Transactional
    public void deleteMeeting(final Long meetingId) {
        final Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_MEETING));
        if (!meeting.getHost().getId().equals(securityUtil.getId())) {
            throw new CustomException(CustomErrorCode.INVALID_HOST);
        }
        meetingRepository.delete(meeting);
    }
}
