package weteam.backend.domain.meeting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import weteam.backend.application.firebase.FirebaseUtil;
import weteam.backend.common.IntegrationTest;
import weteam.backend.domain.meeting.dto.time_slot.RequestTimeSlotDto;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.meeting_user.MeetingUserRepository;
import weteam.backend.domain.meeting_user.entity.MeetingUser;
import weteam.backend.domain.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class MeetingUserControllerTest {
  private String token;
  private final String END_POINT = "/api/meeting-users";
  @Autowired
  MockMvc mockMvc;
  @Autowired
  protected ObjectMapper mapper = new ObjectMapper();
  @Autowired
  FirebaseUtil firebaseUtil;
  @Autowired
  MeetingUserRepository meetingUserRepository;
  @Autowired
  MeetingRepository meetingRepository;

  @BeforeEach
  void setUp() {
    token = "Bearer " + firebaseUtil.createIdToken();
    this.mapper.registerModule(new JavaTimeModule());
  }

  @Nested
  @DisplayName("성공")
  class Success {
    @Test
    @DisplayName("약속_초대용_hashId_조회")
    void readHashedId() throws Exception {
      Meeting meeting = meetingRepository.findById(1L).orElseThrow(NoSuchElementException::new);

      mockMvc.perform(get(END_POINT + "/1")
              .header("Authorization", token)
          ).andExpect(status().isOk())
          .andExpect(content().string(meeting.getHashedId()));
    }

    @Test
    @DisplayName("약속_초대_수락")
    void acceptInvite() throws Exception {
      Meeting meeting = meetingRepository.findById(2L).orElseThrow(NoSuchElementException::new);

      mockMvc.perform(patch(END_POINT + "/" + meeting.getHashedId())
          .header("Authorization", token)
      ).andExpect(status().isNoContent());

      Meeting joinedMeeting = meetingRepository.findById(2L).orElseThrow(NoSuchElementException::new);
      assertThat(joinedMeeting.getMeetingUserList().get(1).getUser().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("시간_수정")
    void updateTimeSlot() throws Exception {
      List<RequestTimeSlotDto> timeSlotDtos = List.of(new RequestTimeSlotDto(LocalDateTime.now(), LocalDateTime.now()));
      String body = mapper.writeValueAsString(timeSlotDtos);

      mockMvc.perform(patch(END_POINT + "/1/time")
          .header("Authorization", token)
          .content(body)
          .contentType(MediaType.APPLICATION_JSON)
      ).andExpect(status().isNoContent());

      MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(1L, 1L).orElseThrow(NoSuchElementException::new);
      assertThat(meetingUser.getTimeSlotList().get(0).getStartedAt()).isEqualTo(timeSlotDtos.get(0).startedAt());
    }

    @Test
    @DisplayName("약속_목록_조회_시_제외_처리")
    void hideMeeting() throws Exception {
      mockMvc.perform(patch(END_POINT + "/1/displayed")
          .header("Authorization", token)
      ).andExpect(status().isNoContent());

      MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(1L, 1L).orElseThrow(NoSuchElementException::new);
      assertThat(meetingUser.isDisplayed()).isFalse();
    }

    @Test
    @DisplayName("약속_탈퇴")
    void quitMeeting() throws Exception {
      mockMvc.perform(patch(END_POINT + "/1/quit")
          .header("Authorization", token)
      ).andExpect(status().isNoContent());

      MeetingUser meetingUser = meetingUserRepository.findByMeetingIdAndUserId(1L, 1L).orElse(null);
      assertThat(meetingUser).isNull();
    }
  }
}