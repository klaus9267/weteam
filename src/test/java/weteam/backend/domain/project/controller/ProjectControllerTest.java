package weteam.backend.domain.project.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.domain.project.dto.CreateProjectDto;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerTest extends BaseIntegrationTest {
  private final String END_POINT = "/api/projects";

  @Test
  @DisplayName("팀플 생성")
  void addProject() throws Exception {
    CreateProjectDto projectDto = new CreateProjectDto("test name", LocalDate.now(), 1L, LocalDate.now(), "test explanation");
    String body = mapper.writeValueAsString(projectDto);

    mockMvc.perform(post(END_POINT)
            .header("Authorization", idToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andDo(print());
  }
}