package weteam.backend.domain.meeting.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.common.DataInitializer;
import weteam.backend.domain.common.pagination.param.MeetingPaginationParam;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDtoV2;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.entity.MeetingUser;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.meeting.repository.MeetingUserRepository;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MeetingUserControllerTest extends BaseIntegrationTest {
  private final String END_POINT = "/api/meeting-users";
  @Autowired
  MeetingUserRepository meetingUserRepository;
  @Autowired
  MeetingRepository meetingRepository;

  @Nested
  class 성공 {
    @Test
    void 약속_초대용_hashId_조회() throws Exception {
      Meeting meeting = testRepository.findMeetingById(1L);

      mockMvc.perform(get(END_POINT + "/" + meeting.getId())
              .header("Authorization", idToken)
          ).andExpect(status().isOk())
          .andExpect(content().string(meeting.getHashedId()));
    }

    @Test
    void 약속_초대_수락() throws Exception {
      Meeting meeting = testRepository.findMeetingById(32L);
      User user = testRepository.findMe();
      mockMvc.perform(patch(END_POINT + "/" + meeting.getHashedId())
          .header("Authorization", idToken)
      ).andExpect(status().isNoContent());

      Meeting foundMeeting = testRepository.findMeetingById(32L);
      assertThat(foundMeeting.getMeetingUserList().get(1).getUser()).isEqualTo(user);
    }

    @Test
    void 시간_수정_v2() throws Exception {
      Meeting meeting = findJoinedMeeting();
      User user = testRepository.findMe();

      List<LocalDateTime> list = new ArrayList<>();
      list.add(LocalDateTime.now());
      list.add(LocalDateTime.now().plusMonths(1));
      RequestTimeSlotDtoV2 slotDtoV2 = new RequestTimeSlotDtoV2(list);
      String body = mapper.writeValueAsString(slotDtoV2);

      mockMvc.perform(patch(END_POINT + "/v2/" + meeting.getId() + "/time")
          .header("Authorization", idToken)
          .content(body)
          .contentType(MediaType.APPLICATION_JSON)
      ).andExpect(status().isNoContent());

      Meeting foundMeeting = testRepository.findMeetingById(meeting.getId());
      assertThat(foundMeeting.getMeetingUserList().get(0).getUser()).isEqualTo(user);
    }

    @Test
    void 약속_목록_조회_시_제외_처리() throws Exception {
      Meeting meeting = findJoinedMeeting();
      User user = DataInitializer.testUser;

      mockMvc.perform(patch(END_POINT + "/" + meeting.getId() + "/displayed")
          .header("Authorization", idToken)
      ).andExpect(status().isNoContent());

      MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(meeting.getId(), user.getId()).orElseThrow(NoSuchElementException::new);
      assertThat(meetingUser.isDisplayed()).isFalse();
    }

    @Test
    void 약속_탈퇴() throws Exception {
      Meeting meeting = findJoinedMeeting();
      assertThat(meeting.getMeetingUserList().size()).isEqualTo(1);

      mockMvc.perform(patch(END_POINT + "/" + meeting.getId() + "/quit")
          .header("Authorization", idToken)
      ).andExpect(status().isNoContent());

      Meeting foundMeeting = testRepository.findMeetingById(meeting.getId());
      assertThat(foundMeeting.getMeetingUserList().size()).isEqualTo(0);
    }
  }

  private Meeting findJoinedMeeting() {
    MeetingPaginationParam param = new MeetingPaginationParam(0, 10, null, null);
    List<Meeting> meetingList = meetingRepository.findAllByUserId(param.toPageable(), DataInitializer.testUser.getId()).getContent();
    Meeting meeting = meetingList.get(0);
    return meeting;
  }
}