package weteam.backend.domain.meeting.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.meeting.repository.MeetingUserRepository;
import weteam.backend.domain.user.entity.User;

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
      Meeting meeting = testRepository.findMeetingById(32L);
      User user = testRepository.findMe();

      mockMvc.perform(patch(END_POINT + "/v2/" + meeting.getId()+"/time")
          .header("Authorization", idToken)
      ).andExpect(status().isNoContent());

      Meeting foundMeeting = testRepository.findMeetingById(32L);
      assertThat(foundMeeting.getMeetingUserList().get(1).getUser()).isEqualTo(user);
    }
  }
}