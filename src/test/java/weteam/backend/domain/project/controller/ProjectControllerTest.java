package weteam.backend.domain.project.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.domain.common.pagination.param.ProjectPaginationParam;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.project.repository.ProjectRepository;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerTest extends BaseIntegrationTest {
  private final String END_POINT = "/api/projects";

  @Autowired
  ProjectRepository projectRepository;
  @Autowired
  UserRepository userRepository;

  @Nested
  class 성공 {
    @Test
    @DisplayName("팀플 생성")
    void addProject() throws Exception {
      CreateProjectDto projectDto = new CreateProjectDto("test name", LocalDate.now(), 1L, LocalDate.now(), "test explanation");
      String body = mapper.writeValueAsString(projectDto);

      mockMvc.perform(post(END_POINT)
              .header("Authorization", idToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body))
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("팀플 목록 조회")
    void readProjectList() throws Exception {
      ProjectPaginationParam paginationParam = new ProjectPaginationParam(0, 10, false, 1L, null, null);

      mockMvc.perform(get(END_POINT)
          .param("page", String.valueOf(paginationParam.getPage()))
          .param("size", String.valueOf(paginationParam.getSize()))
          .param("done", String.valueOf(paginationParam.isDone()))
          .param("userId", String.valueOf(paginationParam.getUserId()))
          .param("field", paginationParam.getField().name())
          .param("direction", paginationParam.getDirection().name())
          .header("Authorization", idToken)
      ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("팀플 단건 조회")
    void readProject() throws Exception {
      List<Project> projectList = projectRepository.findAll();
      List<Long> list = new ArrayList<>();
      A:
      for (long i = 0; i < projectList.size(); i++) {
        List<ProjectUser> projectUserList = projectList.get((int) i).getProjectUserList();
        for (ProjectUser projectUser : projectUserList) {
          if (projectUser.getUser().getId().equals(1L)) {
            list.add(projectList.get((int) i).getId());
            break A;
          }
        }
      }
      if (!list.isEmpty()) {
        mockMvc.perform(get(END_POINT + "/" + list.get(0))
            .header("Authorization", idToken)
        ).andExpect(status().isOk());
      }
    }

    @Test
    @DisplayName("팀플 진행 상황 변경")
    void updateDone() throws Exception {
      User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
      CreateProjectDto projectDto = new CreateProjectDto("test name", LocalDate.now(), 1L, LocalDate.now(), "test explanation");
      Project project = Project.from(projectDto, user);

      Project savedProject = projectRepository.save(project);

      mockMvc.perform(patch(END_POINT + "/" + savedProject.getId()+"/done")
          .header("Authorization", idToken)
      ).andExpect(status().isNoContent());

      Project foundProject = projectRepository.findById(savedProject.getId()).orElseThrow(RuntimeException::new);
      assertThat(foundProject.isDone()).isTrue();
    }
  }

  @Nested
  class 실패 {
    @Nested
    class 팀플_생성 {
      @Test
      void name_NULL() throws Exception {
        CreateProjectDto projectDto = new CreateProjectDto(null, LocalDate.now(), 1L, LocalDate.now(), "test explanation");
        String body = mapper.writeValueAsString(projectDto);

        mockMvc.perform(post(END_POINT)
                .header("Authorization", idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
      }
    }
  }

//  private
}