package weteam.backend.domain.meeting.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.common.TestParam;
import weteam.backend.domain.meeting.dto.meeting.CreateMeetingDto;
import weteam.backend.domain.meeting.dto.meeting.UpdateMeetingDto;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.repository.MeetingRepository;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MeetingControllerTest extends BaseIntegrationTest {
  private final String END_POINT = "/api/meetings";
  @Autowired
  MeetingRepository meetingRepository;

  @Nested
  class 성공 {
    @Test
    void 약속_생성() throws Exception {
      CreateMeetingDto meetingDto = new CreateMeetingDto("titlesdsd", LocalDateTime.now(), 2L, LocalDateTime.now(), null);
      String body = mapper.writeValueAsString(meetingDto);

      mockMvc.perform(post(END_POINT)
              .header("Authorization", idToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.title").value(meetingDto.title()))
          .andExpect(jsonPath("$.imageId").value(meetingDto.imageId()));
    }

    @Test
    void 약속_목록_조회() throws Exception {
      MultiValueMap<String, String> params = TestParam.make();
      User user = testRepository.findMe();

      mockMvc.perform(get(END_POINT)
              .header("Authorization", idToken)
              .params(params)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.meetingDtoList").exists());
    }

    @Test
    void 약속_상세_조회_v2() throws Exception {
      Meeting meeting = testRepository.findMeetingById(2L);
      mockMvc.perform(get(END_POINT + "/v2/detail")
              .header("Authorization", idToken)
              .param("meetingId", String.valueOf(2))
              .param("minimum", String.valueOf(0))
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(meeting.getId()))
          .andExpect(jsonPath("$.title").value(meeting.getTitle()))
          .andExpect(jsonPath("$.imageId").value(meeting.getImageId()));
    }

    @Test
    void 약속_수정() throws Exception {
      UpdateMeetingDto meetingDto = new UpdateMeetingDto("test title2", null, null);
      String body = mapper.writeValueAsString(meetingDto);

      mockMvc.perform(patch(END_POINT + "/2")
          .header("Authorization", idToken)
          .content(body)
          .contentType(MediaType.APPLICATION_JSON)
      ).andExpect(status().isNoContent());

      Meeting meeting = testRepository.findMeetingById(2L);
      assertThat(meeting.getTitle()).isEqualTo(meetingDto.title());
    }

    @Test
    void 약속_삭제() throws Exception {
      mockMvc.perform(delete(END_POINT + "/2")
          .header("Authorization", idToken)
      ).andExpect(status().isNoContent());

      Optional<Meeting> meeting = meetingRepository.findById(2L);
      assertThat(meeting).isEmpty();
    }
  }
}
