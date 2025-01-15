package weteam.backend.domain.project.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import weteam.backend.application.firebase.FirebaseUtil;
import weteam.backend.common.IntegrationTest;
import weteam.backend.domain.common.pagination.param.ProjectPaginationParam;
import weteam.backend.domain.project.ProjectRepository;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.dto.UpdateProjectDto;
import weteam.backend.domain.project.entity.Project;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ProjectControllerTest {
  private String token;
  private final String END_POINT = "/api/projects";
  protected ObjectMapper mapper = new ObjectMapper();
  @Autowired
  FirebaseUtil firebaseUtil;
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ProjectRepository projectRepository;

  @BeforeEach
  void setUp() {
    token = "Bearer " + firebaseUtil.createIdToken();
    this.mapper.registerModule(new JavaTimeModule());
  }

  @Nested
  @DisplayName("성공")
  class Success {
    @Test
    @DisplayName("팀플_생성")
    void createProject() throws Exception {
      CreateProjectDto projectDto = new CreateProjectDto("test name", LocalDate.now(), 1L, LocalDate.now(), "test explanation");
      String body = mapper.writeValueAsString(projectDto);

      mockMvc.perform(post(END_POINT)
          .header("Authorization", token)
          .contentType(MediaType.APPLICATION_JSON)
          .content(body)
      ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("팀플_목록_조회")
    void findProjects() throws Exception {
      ProjectPaginationParam paginationParam = new ProjectPaginationParam(0, 10, false, 1L, null, null);

      mockMvc.perform(get(END_POINT)
          .param("page", String.valueOf(paginationParam.getPage()))
          .param("size", String.valueOf(paginationParam.getSize()))
          .param("done", String.valueOf(paginationParam.isDone()))
          .param("userId", String.valueOf(paginationParam.getUserId()))
          .param("field", paginationParam.getField().name())
          .param("direction", paginationParam.getDirection().name())
          .header("Authorization", token)
      ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("팀플_단건_조회")
    void findProject() throws Exception {
      mockMvc.perform(get(END_POINT + "/1")
          .header("Authorization", token)
      ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("팀플_진행_상황_변경")
    void updateProjectProgress() throws Exception {
      mockMvc.perform(patch(END_POINT + "/1/done")
          .header("Authorization", token)
      ).andExpect(status().isNoContent());

      Project foundProject = projectRepository.findById(1L).orElseThrow(RuntimeException::new);
      assertThat(foundProject.isDone()).isTrue();
    }

    @Nested
    class 팀플_수정 {
      private void callApiRequestForUpdate(final UpdateProjectDto projectDto, final Project project) throws Exception {
        String body = mapper.writeValueAsString(projectDto);

        mockMvc.perform(patch(END_POINT + "/" + project.getId())
            .header("Authorization", token)
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
      @DisplayName("이름만_입력")
      void name() throws Exception {
        Project project = projectRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        UpdateProjectDto projectDto = new UpdateProjectDto("test name1", null, null, null);

        callApiRequestForUpdate(projectDto, project);
      }

      @Test
      @DisplayName("시작일만_입력")
      void startedAt() throws Exception {
        Project project = projectRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        UpdateProjectDto projectDto = new UpdateProjectDto(null, LocalDate.now(), null, null);

        callApiRequestForUpdate(projectDto, project);
      }

      @Test
      @DisplayName("종료일만_입력")
      void endedAt() throws Exception {
        Project project = projectRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        UpdateProjectDto projectDto = new UpdateProjectDto(null, null, LocalDate.now(), null);

        callApiRequestForUpdate(projectDto, project);
      }

      @Test
      @DisplayName("설명만_입력")
      void explanation() throws Exception {
        Project project = projectRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        UpdateProjectDto projectDto = new UpdateProjectDto(null, null, null, "test explanation1");

        callApiRequestForUpdate(projectDto, project);
      }
    }

    @Test
    @DisplayName("팀플_호스트_넘기기")
    void updateHost() throws Exception {
      Project project = projectRepository.findById(1L).orElseThrow(NoSuchElementException::new);
      long userId = 2L;

      mockMvc.perform(patch(END_POINT + "/" + project.getId() + "/" + userId)
          .header("Authorization", token)
      ).andExpect(status().isNoContent());

      Project foundProject = projectRepository.findById(project.getId()).orElseThrow(RuntimeException::new);
      assertThat(project).extracting(
          Project::getId,
          Project::getName,
          Project::getExplanation,
          Project::getHashedId,
          project1 -> project1.getHost().getId()
      ).containsExactly(
          foundProject.getId(),
          foundProject.getName(),
          foundProject.getExplanation(),
          foundProject.getHashedId(),
          userId
      );
    }

    @Test
    @DisplayName("팀플_삭제")
    void deleteProject() throws Exception {
      mockMvc.perform(delete(END_POINT + "/1")
          .header("Authorization", token)
      ).andExpect(status().isNoContent());
    }
  }

  @Nested
  @DisplayName("실패")
  class Fail {
    @Nested
    @DisplayName("팀플_생성")
    class CreateProject {
      private void callApiForBadRequestWhenCreate(final FailProjectDtoForCreate failProjectDtoForCreate) throws Exception {
        String body = mapper.writeValueAsString(failProjectDtoForCreate);

        mockMvc.perform(post(END_POINT)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
      }

      @Test
      @DisplayName("이름_누락")
      void nameNull() throws Exception {
        final FailProjectDtoForCreate projectDto = new FailProjectDtoForCreate(null, LocalDate.now(), 1L, LocalDate.now(), "explanation");
        this.callApiForBadRequestWhenCreate(projectDto);
      }

      @Test
      @DisplayName("시작일_누락")
      void startedAtNull() throws Exception {
        final FailProjectDtoForCreate projectDto = new FailProjectDtoForCreate("name", null, 1L, LocalDate.now(), "explanation");
        this.callApiForBadRequestWhenCreate(projectDto);
      }

      @Test
      @DisplayName("사진_누락")
      void imageIdNull() throws Exception {
        final FailProjectDtoForCreate projectDto = new FailProjectDtoForCreate("name", LocalDate.now(), null, LocalDate.now(), "explanation");
        this.callApiForBadRequestWhenCreate(projectDto);
      }

      @Test
      @DisplayName("종료일_누락")
      void endedAtNull() throws Exception {
        final FailProjectDtoForCreate projectDto = new FailProjectDtoForCreate("name", LocalDate.now(), 1L, null, "explanation");
        this.callApiForBadRequestWhenCreate(projectDto);
      }

      @Test
      @DisplayName("설명_누락")
      void explanationNull() throws Exception {
        final FailProjectDtoForCreate projectDto = new FailProjectDtoForCreate("name", LocalDate.now(), 1L, LocalDate.now(), null);
        this.callApiForBadRequestWhenCreate(projectDto);
      }

      @Test
      @DisplayName("전부_누락")
      void allNull() throws Exception {
        final FailProjectDtoForCreate projectDto = new FailProjectDtoForCreate(null, null, null, null, null);
        this.callApiForBadRequestWhenCreate(projectDto);
      }
    }

    @Nested
    @DisplayName("목록_조회")
    class findProjects {
      private void callApiForBadRequestWhenRead(final Integer page, final Integer size, final Boolean isDone, Long userId) throws Exception {
        mockMvc.perform(get(END_POINT)
            .param("page", page != null ? String.valueOf(page) : null)
            .param("size", size != null ? String.valueOf(size) : null)
            .param("done", isDone != null ? String.valueOf(isDone) : null)
            .param("userId", userId != null ? String.valueOf(userId) : null)
            .header("Authorization", token)
        ).andExpect(status().isInternalServerError());
      }

      @Test
      @DisplayName("페이지_번호_누락")
      void pageNull() throws Exception {
        this.callApiForBadRequestWhenRead(null, 10, false, 1L);
      }

      @Test
      @DisplayName("페이지_크기_누락")
      void sizeNull() throws Exception {
        this.callApiForBadRequestWhenRead(0, null, false, 1L);
      }
    }

    @Nested
    @DisplayName("팀플_단건_조회")
    class FindProject {
      @Test
      @DisplayName("없는_아이디")
      void notFoundProject() throws Exception {
        mockMvc.perform(get(END_POINT + "/11111")
                .header("Authorization", token))
            .andExpect(status().isNotFound());
      }
    }

    @Nested
    @DisplayName("팀플_진행_상황_변경")
    class UpdateProgress {
      @Test
      @DisplayName("없는_프로젝트")
      void notFoundProject() throws Exception {
        mockMvc.perform(patch(END_POINT + "/11111/done")
                .header("Authorization", token))
            .andExpect(status().isNotFound());
      }
    }

    @Nested
    @DisplayName("팀플_수정")
    class UpdateProject {
      private void callApiForBadRequestWhenUpdate(final FailProjectDtoForUpdate failProjectDtoForUpdate) throws Exception {
        String body = mapper.writeValueAsString(failProjectDtoForUpdate);

        mockMvc.perform(patch(END_POINT + "/1")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        ).andExpect(status().isBadRequest());
      }

      @Test
      @DisplayName("요청_null")
      void allNull() throws Exception {
        final FailProjectDtoForUpdate projectDto = new FailProjectDtoForUpdate(null, null, null, null);
        this.callApiForBadRequestWhenUpdate(projectDto);
      }
    }

    @Nested
    @DisplayName("호스트_넘기기")
    class ChangeHost {
      @Test
      @DisplayName("프로젝트를_찾을_수_없음")
      void projectIdNotFound() throws Exception {
        mockMvc.perform(patch(END_POINT + "/1111111/32")
                .header("Authorization", token))
            .andExpect(status().isNotFound());
      }

      @Test
      @DisplayName("사용자를_찾을_수_없음")
      void userIdNotFound() throws Exception {
        mockMvc.perform(patch(END_POINT + "/1/111111")
                .header("Authorization", token))
            .andExpect(status().isNotFound());
      }

      @Test
      @DisplayName("호스트_아님")
      void notHost() throws Exception {
        mockMvc.perform(patch(END_POINT + "/2/2")
                .header("Authorization", token))
            .andExpect(status().isBadRequest());
      }
    }

    @Nested
    @DisplayName("팀플_삭제")
    class DeleteProject {
      @Test
      @DisplayName("호스트_아님")
      void notHost() throws Exception {
        mockMvc.perform(delete(END_POINT + "/2")
                .header("Authorization", token))
            .andExpect(status().isBadRequest());
      }

      @Test
      @DisplayName("없는 프로젝트")
      void notFoundProject() throws Exception {
        mockMvc.perform(delete(END_POINT + "/333")
                .header("Authorization", token))
            .andExpect(status().isNotFound());
      }
    }
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  class FailProjectDtoForCreate {
    private final Object name;
    private final Object startedAt;
    private final Object imageId;
    private final Object endedAt;
    private final Object explanation;

    FailProjectDtoForCreate(final String name, final LocalDate startedAt, final Long imageId, final LocalDate endedAt, final String explanation) {
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

    FailProjectDtoForUpdate(final String name, final LocalDate startedAt, final LocalDate endedAt, final String explanation) {
      this.name = name;
      this.startedAt = startedAt;
      this.endedAt = endedAt;
      this.explanation = explanation;
    }
  }
}