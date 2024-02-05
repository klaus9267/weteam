package weteam.backend.domain.meeting;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.common.pagination.param.MeetingPaginationParam;
import weteam.backend.domain.meeting.dto.RequestTimeSlotDto;
import weteam.backend.domain.meeting.dto.meeting.*;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.entity.MeetingUser;
import weteam.backend.domain.meeting.entity.TimeSlot;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.meeting.repository.MeetingUserRepository;
import weteam.backend.domain.meeting.repository.TimeSlotRepository;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.repository.ProjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final TimeSlotRepository timeSlotRepository;
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
    public void addTimeSlot(final List<RequestTimeSlotDto> timeSlotDtoList, final Long meetingId) {
        this.validateTimeSlot(timeSlotDtoList);
        final MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
        if (!meetingUser.getTimeSlotList().isEmpty()) {
            throw new CustomException(CustomErrorCode.BAD_REQUEST, "이미 시간을 설정했습니다. 시간 수정 api를 사용해주세요");
        }
        final List<TimeSlot> timeSlotList = TimeSlot.from(timeSlotDtoList, meetingUser);
        timeSlotRepository.saveAll(timeSlotList);
    }

    @Transactional
    public void updateTimeSlot(final List<RequestTimeSlotDto> timeSlotDtoList, final Long meetingId) {
        this.validateTimeSlot(timeSlotDtoList);
        final MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meetingId, securityUtil.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND));
        final List<TimeSlot> timeSlotList = TimeSlot.from(timeSlotDtoList, meetingUser);
        timeSlotRepository.deleteAllByMeetingUser(meetingUser);
        timeSlotRepository.saveAll(timeSlotList);
    }

    @Transactional
    public void updateMeeting(final UpdateMeetingDto meetingDto, final Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_MEETING));
        if (!meeting.getHost().getId().equals(securityUtil.getId())) {
            throw new CustomException(CustomErrorCode.INVALID_HOST);
        }
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

    private void validateTimeSlot(final List<RequestTimeSlotDto> timeSlotDtoList) {
        for (int i = 0; i < timeSlotDtoList.size(); i++) {
            for (int j = i + 1; j < timeSlotDtoList.size(); j++) {
                if (timeSlotDtoList.get(i).startedAt().isBefore(timeSlotDtoList.get(j).endedAt()) && timeSlotDtoList.get(j).startedAt().isBefore(timeSlotDtoList.get(i).endedAt())) {
                    throw new CustomException(CustomErrorCode.BAD_REQUEST, "겹치는 시간대가 있습니다");
                }
            }
        }
    }
}
