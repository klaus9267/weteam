package weteam.backend.domain.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import weteam.backend.application.firebase.FirebaseUtil;
import weteam.backend.common.IntegrationTest;
import weteam.backend.domain.project.ProjectRepository;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project_user.ProjectUserRepository;
import weteam.backend.domain.project_user.entity.ProjectUser;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@Import(ProjectFixture.class)
class ProjectUserControllerTest {
  private String token;
  String END_POINT = "/api/project-users";
  @Autowired
  MockMvc mockMvc;
  @Autowired
  protected ObjectMapper mapper = new ObjectMapper();
  @Autowired
  FirebaseUtil firebaseUtil;
  @Autowired
  ProjectRepository projectRepository;
  @Autowired
  ProjectUserRepository projectUserRepository;
  @Autowired
  ProjectFixture projectFixture;

  @BeforeEach
  void setUp() {
    token = "Bearer " + firebaseUtil.createIdToken();
    this.mapper.registerModule(new JavaTimeModule());
  }

  @Nested
  @DisplayName("성공")
  class Success {
    @Test
    @DisplayName("팀원_초대용_hashedId_조회")
    void readHashId() throws Exception {
      Project project = projectRepository.findById(1L).orElseThrow(NoSuchElementException::new);

      ResultActions actions = mockMvc.perform(get(END_POINT + "/" + project.getId() + "/invite")
          .header("Authorization", token)
      ).andExpect(status().isOk());

      actions.andExpect(content().string(project.getHashedId()));
    }

    @Test
    @DisplayName("팀원_목록_조회")
    void readProjectUsers() throws Exception {
      Project project = projectFixture.joinProject(2L, 1L);

      ResultActions actions = mockMvc.perform(get(END_POINT + "/" + project.getId())
          .header("Authorization", token)
      ).andExpect(status().isOk());

      for (int i = 0; i < project.getProjectUserList().size(); i++) {
        ProjectUser projectUser = project.getProjectUserList().get(i);
        actions.andExpect(jsonPath("$[" + i + "].id").value(projectUser.getId()));
        actions.andExpect(jsonPath("$[" + i + "].role").value(projectUser.getRole()));
      }
    }

    @Test
    @DisplayName("담당_역할_변경")
    void updateRole() throws Exception {
      Project project = projectRepository.findById(1L).orElseThrow(NoSuchElementException::new);
      String role = "test tole";

      mockMvc.perform(patch(END_POINT)
          .header("Authorization", token)
          .param("projectId", String.valueOf(1))
          .param("role", role)
      ).andExpect(status().isNoContent());

      ProjectUser projectUser = projectUserRepository.findByProjectIdAndUserId(1L, 1L).orElseThrow(NoSuchElementException::new);
      assertThat(projectUser).extracting(
          ProjectUser::getId,
          ProjectUser::getProject,
          ProjectUser::getRole
      ).containsExactly(
          project.getProjectUserList().get(0).getId(),
          project,
          role
      );
    }

    @Test
    @DisplayName("초대_수락")
    void acceptInvite() throws Exception {
      Project project = projectRepository.findById(2L).orElseThrow(NoSuchElementException::new);

      mockMvc.perform(patch(END_POINT + "/" + project.getHashedId())
          .header("Authorization", token)
      ).andExpect(status().isNoContent());

      Project foundProject = projectRepository.findById(project.getId()).orElseThrow(NoSuchElementException::new);
      assertThat(foundProject.getProjectUserList().get(1)).isNotNull();
    }

    @Test
    @DisplayName("팀원_강퇴")
    void kickUser() throws Exception {
      Project project = projectFixture.joinProject(2L, 1L);

      mockMvc.perform(delete(END_POINT)
          .header("Authorization", token)
          .param("projectUserIdList",
              String.valueOf(project.getProjectUserList().get(1).getId()))
      ).andExpect(status().isNoContent());

      List<ProjectUser> projectUserList = projectUserRepository.findAllByProjectId(1L);
      assertThat(projectUserList).hasSize(1);
    }

    @Test
    @DisplayName("팀플_탈퇴")
    void quitProject() throws Exception {
      projectFixture.joinProject(1L, 2L);

      mockMvc.perform(delete(END_POINT + "/2")
          .header("Authorization", token)
      ).andExpect(status().isNoContent());

      List<ProjectUser> projectUserList = projectUserRepository.findAllByProjectId(2L);
      assertThat(projectUserList).hasSize(1);
    }
  }

  @Nested
  @DisplayName("실패")
  class Fail {
    @Nested
    @DisplayName("팀원_초대용_hashedId_조회")
    class ReadHashedId {
      @Test
      @DisplayName("프로젝트_없음")
      void notFound() throws Exception {
        mockMvc.perform(get(END_POINT + "/111/invite")
            .header("Authorization", token)
        ).andExpect(status().isNotFound());
      }
    }

    @Nested
    @DisplayName("팀원_목록_조회")
    class ReadProjectUsers {
      @Test
      @DisplayName("프로젝트_없음")
      void notFound() throws Exception {
        mockMvc.perform(get(END_POINT + "/111")
            .header("Authorization", token)
        ).andExpect(status().isNotFound());
      }
    }
  }

  @Nested
  @DisplayName("담당_역할_변경")
  class UpdateRole {
    @Test
    @DisplayName("프로젝트_없음")
    void notFound() throws Exception {
      mockMvc.perform(patch(END_POINT)
          .header("Authorization", token)
          .param("projectId", String.valueOf(2))
          .param("role", "test role")
      ).andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("초대_수락")
  class AcceptInvite {
    @Test
    @DisplayName("초대_수락")
    void acceptInvite() throws Exception {
      mockMvc.perform(patch(END_POINT + "/test hashedId")
          .header("Authorization", token)
      ).andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("팀원_강퇴")
  class KickUser {
    @Test
    @DisplayName("param_오타_String")
    void invalidProjectUserId() throws Exception {
      projectFixture.joinProject(2L, 1L);

      mockMvc.perform(delete(END_POINT)
          .header("Authorization", token)
          .param("projectUserIdList", "asd")
      ).andExpect(status().isInternalServerError());
    }
  }
}
