package weteam.backend.domain.project.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.common.DataInitializer;
import weteam.backend.domain.common.pagination.param.ProjectPaginationParam;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.entity.ProjectUser;
import weteam.backend.domain.project.repository.ProjectRepository;
import weteam.backend.domain.project.repository.ProjectUserRepository;
import weteam.backend.domain.user.entity.User;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProjectUserControllerTest extends BaseIntegrationTest {
  private final String END_POINT = "/api/project-users";

  @Autowired
  ProjectRepository projectRepository;
  @Autowired
  ProjectUserRepository projectUserRepository;

  @Nested
  class 성공 {
    @Test
    void 팀원_초대용_hashedId_조회() throws Exception {
      Project project = testRepository.saveProject();

      mockMvc.perform(get(END_POINT + "/" + project.getId() + "/invite")
              .header("Authorization", idToken)
          ).andExpect(status().isOk())
          .andExpect(content().string(project.getHashedId()));
    }

    @Test
    void 팀원_목록_조회() throws Exception {
      Project project = findJoinedProject();

      ResultActions actions = mockMvc.perform(get(END_POINT + "/" + project.getId())
          .header("Authorization", idToken)
      ).andExpect(status().isOk());

      for (int i = 0; i < project.getProjectUserList().size(); i++) {
        ProjectUser projectUser = project.getProjectUserList().get(i);
        actions.andExpect(jsonPath("$[" + i + "].id").value(projectUser.getId()));
        actions.andExpect(jsonPath("$[" + i + "].role").value(projectUser.getRole()));
        actions.andExpect(jsonPath("$[" + i + "].enable").value(projectUser.isEnable()));
      }
    }

    @Test
    void 담당_역할_변경() throws Exception {
      Project project = findJoinedProject();
      String role = "test tole";

      mockMvc.perform(patch(END_POINT)
          .header("Authorization", idToken)
          .param("projectId", String.valueOf(project.getId()))
          .param("role", role)
      ).andExpect(status().isNoContent());

      ProjectUser projectUser = findProjectUserById(project.getProjectUserList().get(0).getId());
      assertThat(projectUser).extracting(
          ProjectUser::getId,
          ProjectUser::getProject,
          ProjectUser::getRole
      ).containsExactly(
          project.getProjectUserList().get(0).getId(),
          project,
          project.getProjectUserList().get(0).getRole()
      );
    }

    @Test
    void 초대_수락() throws Exception {
      Project project = testRepository.saveProject();

      mockMvc.perform(patch(END_POINT + "/" + project.getHashedId())
          .header("Authorization", idToken)
      ).andExpect(status().isNoContent());

      Project foundProject = projectRepository.findById(project.getId()).orElseThrow(NoSuchElementException::new);
      assertThat(foundProject.getProjectUserList().get(0)).isNotNull();
    }

    @Test
    void 팀원_강퇴() throws Exception {
      Project project = findJoinedProject();
      project.

      mockMvc.perform(patch(END_POINT)
          .header("Authorization", idToken)
      ).andExpect(status().isNoContent());
    }
  }

  private ProjectUser findProjectUserById(Long id) {
    return projectUserRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  private Project findJoinedProject() {
    User user = DataInitializer.testUser;
    ProjectPaginationParam param = new ProjectPaginationParam(0, 10, false, user.getId(), null, null);
    List<Project> projectList = projectRepository.findAllByUserIdAndIsDone(param.toPageable(), user.getId(), false).getContent();
    if (projectList.isEmpty()) {
      throw new NoSuchElementException("not found project");
    }
    return projectList.get(0);
  }
}
