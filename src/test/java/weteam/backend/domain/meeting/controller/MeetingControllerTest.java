package weteam.backend.domain.meeting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;
import weteam.backend.application.firebase.FirebaseUtil;
import weteam.backend.common.IntegrationTest;
import weteam.backend.common.TestParam;
import weteam.backend.domain.meeting.dto.meeting.CreateMeetingDto;
import weteam.backend.domain.meeting.dto.meeting.UpdateMeetingDto;
import weteam.backend.domain.meeting.entity.Meeting;
import weteam.backend.domain.meeting.repository.MeetingRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class MeetingControllerTest {
  private String token;
  private final String END_POINT = "/api/meetings";
  @Autowired
  MockMvc mockMvc;
  @Autowired
  protected ObjectMapper mapper = new ObjectMapper();
  @Autowired
  FirebaseUtil firebaseUtil;
  @Autowired
  MeetingRepository meetingRepository;

  @BeforeEach
  void setUp() {
    token = "Bearer " + firebaseUtil.createIdToken();
    this.mapper.registerModule(new JavaTimeModule());
  }

  @Nested
  class 성공 {
    @Test
    @DisplayName("약속_생성_no_project")
    void createMeetingWithoutProject() throws Exception {
      CreateMeetingDto meetingDto = new CreateMeetingDto("test title", LocalDateTime.now(), 2L, LocalDateTime.now(), null);
      String body = mapper.writeValueAsString(meetingDto);

      mockMvc.perform(post(END_POINT)
              .header("Authorization", token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.title").value(meetingDto.title()))
          .andExpect(jsonPath("$.imageId").value(meetingDto.imageId()));
    }

    @Test
    @DisplayName("약속_생성_with_project")
    void createMeetingWithProject() throws Exception {
      CreateMeetingDto meetingDto = new CreateMeetingDto("test title", LocalDateTime.now(), 2L, LocalDateTime.now(), 1L);
      String body = mapper.writeValueAsString(meetingDto);

      mockMvc.perform(post(END_POINT)
              .header("Authorization", token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.title").value(meetingDto.title()))
          .andExpect(jsonPath("$.imageId").value(meetingDto.imageId()));
    }

    @Test
    @DisplayName("약속_목록_조회")
    void readMeetings() throws Exception {
      MultiValueMap<String, String> params = TestParam.make();
      mockMvc.perform(get(END_POINT)
              .header("Authorization", token)
              .params(params)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.meetingDtoList").exists());
    }

    @Test
    @DisplayName("약속_수정")
    void UpdateMeeting() throws Exception {
      UpdateMeetingDto meetingDto = new UpdateMeetingDto("test title1", null, null);
      String body = mapper.writeValueAsString(meetingDto);

      mockMvc.perform(patch(END_POINT + "/1")
          .header("Authorization", token)
          .content(body)
          .contentType(MediaType.APPLICATION_JSON)
      ).andExpect(status().isNoContent());

      Meeting meeting = meetingRepository.findById(1L).orElseThrow(NoSuchElementException::new);
      assertThat(meeting.getTitle()).isEqualTo(meetingDto.title());
    }

    @Test
    @DisplayName("약속_삭제")
    void deleteMeeting() throws Exception {
      mockMvc.perform(delete(END_POINT + "/1")
          .header("Authorization", token)
      ).andExpect(status().isNoContent());

      Optional<Meeting> foundMeeting = meetingRepository.findById(1L);
      assertThat(foundMeeting).isEmpty();
    }
  }
}
