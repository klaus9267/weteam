package weteam.backend.domain.project.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.common.DataInitializer;
import weteam.backend.domain.common.pagination.param.ProjectPaginationParam;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.UpdateProjectDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.project.repository.ProjectRepository;

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
      Project savedProject = saveProject();

      mockMvc.perform(patch(END_POINT + "/" + savedProject.getId() + "/done")
          .header("Authorization", idToken)
      ).andExpect(status().isNoContent());

      Project foundProject = projectRepository.findById(savedProject.getId()).orElseThrow(RuntimeException::new);
      assertThat(foundProject.isDone()).isTrue();
    }

    @Nested
    class 팀플_수정 {
      private void callApiRequestForUpdate(final UpdateProjectDto projectDto, final Project project) throws Exception {
        String body = mapper.writeValueAsString(projectDto);

        mockMvc.perform(patch(END_POINT + "/" + project.getId())
            .header("Authorization", idToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        ).andExpect(status().isNoContent());

        Project foundProject = projectRepository.findById(project.getId()).orElseThrow(RuntimeException::new);
        assertThat(foundProject).extracting(
            Project::getName,
            Project::getStartedAt,
            Project::getEndedAt,
            Project::getExplanation
        ).containsExactly(
            projectDto.name() == null ? project.getName() : projectDto.name(),
            projectDto.startedAt() == null ? project.getStartedAt() : projectDto.startedAt(),
            projectDto.endedAt() == null ? project.getEndedAt() : projectDto.endedAt(),
            projectDto.explanation() == null ? project.getExplanation() : projectDto.explanation()
        );
      }

      @Test
      void name_only() throws Exception {
        Project savedProject = saveProject();
        UpdateProjectDto projectDto = new UpdateProjectDto("test name1", null, null, null);

        callApiRequestForUpdate(projectDto, savedProject);
      }

      @Test
      void startedAt_only() throws Exception {
        Project savedProject = saveProject();
        UpdateProjectDto projectDto = new UpdateProjectDto(null, LocalDate.now(), null, null);

        callApiRequestForUpdate(projectDto, savedProject);
      }

      @Test
      void endedAt_only() throws Exception {
        Project savedProject = saveProject();
        UpdateProjectDto projectDto = new UpdateProjectDto(null, null, LocalDate.now(), null);

        callApiRequestForUpdate(projectDto, savedProject);
      }

      @Test
      void explanation_only() throws Exception {
        Project savedProject = saveProject();
        UpdateProjectDto projectDto = new UpdateProjectDto(null, null, null, "test explanation1");

        callApiRequestForUpdate(projectDto, savedProject);
      }
    }
  }

  @Nested
  class 실패 {
    @Nested
    class 팀플_생성 {
      private void callApiForBadRequestWhenCreate(final FailProjectDtoForCreate failProjectDtoForCreate) throws Exception {
        String body = mapper.writeValueAsString(failProjectDtoForCreate);

        mockMvc.perform(post(END_POINT)
                .header("Authorization", idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
      }

      @Test
      void name_NULL() throws Exception {
        final FailProjectDtoForCreate projectDto = new FailProjectDtoForCreate("name", LocalDate.now(), 1L, LocalDate.now(), "explanation");
        ReflectionTestUtils.setField(projectDto, "name", null);

        this.callApiForBadRequestWhenCreate(projectDto);
      }

      @Test
      void startedAt_NULL() throws Exception {
        final FailProjectDtoForCreate projectDto = new FailProjectDtoForCreate("name", LocalDate.now(), 1L, LocalDate.now(), "explanation");
        ReflectionTestUtils.setField(projectDto, "startedAt", null);

        this.callApiForBadRequestWhenCreate(projectDto);
      }

      @Test
      void imageId_NULL() throws Exception {
        final FailProjectDtoForCreate projectDto = new FailProjectDtoForCreate("name", LocalDate.now(), 1L, LocalDate.now(), "explanation");
        ReflectionTestUtils.setField(projectDto, "imageId", null);

        this.callApiForBadRequestWhenCreate(projectDto);
      }

      @Test
      void endedAt_NULL() throws Exception {
        final FailProjectDtoForCreate projectDto = new FailProjectDtoForCreate("name", LocalDate.now(), 1L, LocalDate.now(), "explanation");
        ReflectionTestUtils.setField(projectDto, "endedAt", null);

        this.callApiForBadRequestWhenCreate(projectDto);
      }

      @Test
      void explanation_NULL() throws Exception {
        final FailProjectDtoForCreate projectDto = new FailProjectDtoForCreate("name", LocalDate.now(), 1L, LocalDate.now(), "explanation");
        ReflectionTestUtils.setField(projectDto, "explanation", null);

        this.callApiForBadRequestWhenCreate(projectDto);
      }

      @Test
      void all_NULL() throws Exception {
        final FailProjectDtoForCreate projectDto = new FailProjectDtoForCreate(null, null, null, null, null);

        this.callApiForBadRequestWhenCreate(projectDto);
      }
    }

    @Nested
    class 팀플_수정 {
      private void callApiForBadRequestWhenUpdate(final FailProjectDtoForUpdate failProjectDtoForUpdate, final Long projectId) throws Exception {
        String body = mapper.writeValueAsString(failProjectDtoForUpdate);

        mockMvc.perform(patch(END_POINT + "/" + projectId)
                .header("Authorization", idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
      }

      @Test
      void all_NULL() throws Exception {
        Project project = saveProject();
        final FailProjectDtoForUpdate projectDto = new FailProjectDtoForUpdate(null, null, null, null);
        ReflectionTestUtils.setField(projectDto, "name", null);

        this.callApiForBadRequestWhenUpdate(projectDto, project.getId());
      }
    }
  }

  private Project saveProject() {
    CreateProjectDto projectDto = new CreateProjectDto("test name", LocalDate.now(), 1L, LocalDate.now(), "test explanation");
    Project project = Project.from(projectDto, DataInitializer.testUser);

    return projectRepository.save(project);
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  class FailProjectDtoForCreate {
    private final Object name;
    private final Object startedAt;
    private final Object imageId;
    private final Object endedAt;
    private final Object explanation;

    public FailProjectDtoForCreate(final String name, final LocalDate startedAt, final Long imageId, final LocalDate endedAt, final String explanation) {
      this.name = name;
      this.startedAt = startedAt;
      this.imageId = imageId;
      this.endedAt = endedAt;
      this.explanation = explanation;
    }
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  class FailProjectDtoForUpdate {
    private final Object name;
    private final Object startedAt;
    private final Object endedAt;
    private final Object explanation;

    public FailProjectDtoForUpdate(final String name, final LocalDate startedAt, final LocalDate endedAt, final String explanation) {
      this.name = name;
      this.startedAt = startedAt;
      this.endedAt = endedAt;
      this.explanation = explanation;
    }
  }
}