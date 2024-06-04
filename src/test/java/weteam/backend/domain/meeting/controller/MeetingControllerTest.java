package weteam.backend.domain.meeting.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.common.TestParam;
import weteam.backend.domain.meeting.dto.meeting.CreateMeetingDto;
import weteam.backend.domain.meeting.repository.MeetingRepository;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
      CreateMeetingDto meetingDto = new CreateMeetingDto("title", LocalDateTime.now(), 2L, LocalDateTime.now(), null);
      String body = mapper.writeValueAsString(meetingDto);

      mockMvc.perform(post(END_POINT)
              .header("Authorization", idToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1L))
          .andExpect(jsonPath("$.title").value(meetingDto.title()))
          .andExpect(jsonPath("$.imageId").value(meetingDto.imageId()));
    }

    @Test
    void 약속_목록_조회() throws Exception {
      MultiValueMap<String, String> params = TestParam.make();
      mockMvc.perform(get(END_POINT)
                  .header("Authorization", idToken)
                  .params(params)
//              .param(param)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$").exists());
    }
  }
}
