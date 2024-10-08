package weteam.backend.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.common.DataInitializer;
import weteam.backend.domain.project.dto.CreateProjectDto;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project.ProjectRepository;
import weteam.backend.domain.user.dto.RequestUserDto;
import weteam.backend.domain.user.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest extends BaseIntegrationTest {
  private final String END_POINT = "/api/users";
  @Autowired
  SecurityUtil securityUtil;
  @Autowired
  UserRepository userRepository;
  @Autowired
  ProjectRepository projectRepository;

  @Nested
  class 성공 {
    @Test
    @DisplayName("내 정보 조회")
    void readMyInfo() throws Exception {
      User user = DataInitializer.testUser;

      mockMvc.perform(get(END_POINT)
              .header("Authorization", idToken)
          ).andExpect(jsonPath("$.id").value(user.getId()))
          .andExpect(jsonPath("$.username").value(user.getUsername()))
          .andExpect(jsonPath("$.organization").value(user.getOrganization()))
          .andExpect(jsonPath("$.introduction").value(user.getIntroduction()))
          .andExpect(status().isOk())
          .andDo(print())
      ;
    }

    @Test
    @DisplayName("다른 사용자 정보 조회")
    void readOtherInfo() throws Exception {
      Random random = new Random();
      int n = random.nextInt(0, 100);
      mockMvc.perform(get(END_POINT + "/" + n)
              .header("Authorization", idToken)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(n));

      User user = userRepository.findByUid(uid).orElseThrow(NoSuchElementException::new);
      assertThat(user).extracting(
          User::getUsername,
          User::getOrganization,
          User::getIntroduction
      ).doesNotContainNull();
    }

    @Nested
    class 사용자_정보_변경 {
      @Test
      void ony_username() throws Exception {
        RequestUserDto userDto = new RequestUserDto("test", null, null);
        this.performRequest(userDto);
      }

      @Test
      void organization_username() throws Exception {
        RequestUserDto userDto = new RequestUserDto(null, "organization", null);
        this.performRequest(userDto);
      }

      @Test
      void introduction_username() throws Exception {
        RequestUserDto userDto = new RequestUserDto(null, null, "introduction");
        this.performRequest(userDto);
      }

      private void performRequest(RequestUserDto userDto) throws Exception {
        String body = mapper.writeValueAsString(userDto);

        mockMvc.perform(patch(END_POINT)
                .content(body)
                .header("Authorization", idToken)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        User user = userRepository.findByUid(uid).orElseThrow(NoSuchElementException::new);
        assertThat(user).extracting(
            User::getUsername,
            User::getOrganization,
            User::getIntroduction
        ).containsExactly(
            userDto.username() == null ? DataInitializer.testUser.getUsername() : userDto.username(),
            userDto.organization() == null ? DataInitializer.testUser.getOrganization() : userDto.organization(),
            userDto.introduction() == null ? DataInitializer.testUser.getIntroduction() : userDto.introduction()
        );
      }
    }

    @Test
    @DisplayName("푸시 알람 수신 변경")
    void changeReceivePermission() throws Exception {
      mockMvc.perform(patch(END_POINT + "/push")
              .header("Authorization", idToken))
          .andExpect(status().isNoContent());

      User user = userRepository.findByUid(uid).orElseThrow(NoSuchElementException::new);
      assertThat(user.isReceivePermission()).isFalse();
    }

    @Test
    @DisplayName("로그아웃")
    void logout() throws Exception {
      mockMvc.perform(patch(END_POINT + "/logout")
              .header("Authorization", idToken))
          .andExpect(status().isNoContent());

      User user2 = userRepository.findByUid(uid).orElseThrow(NoSuchElementException::new);
      assertThat(user2.isLogin()).isFalse();
    }

    @Test
    @DisplayName("사용자 탈퇴")
    void quit() throws Exception {
      mockMvc.perform(delete(END_POINT)
              .header("Authorization", idToken))
          .andExpect(status().isNoContent());

      List<User> userList = userRepository.findAll();
      assertThat(userList.contains(DataInitializer.testUser)).isFalse();
    }
  }

  @Nested
  class 실패 {
    @Test
    public void 다른_사용자_조회_없는_아이디() throws Exception {

      mockMvc.perform(get(END_POINT + "/444")
          .header("Authorization", idToken)
      ).andExpect(status().isNotFound());
    }

    @Test
    public void 사용자_정보_변경_NULL() throws Exception {
      RequestUserDto userDto = new RequestUserDto(null, null, null);
      String body = mapper.writeValueAsString(userDto);

      mockMvc.perform(patch(END_POINT)
              .content(body)
              .header("Authorization", idToken)
              .contentType(MediaType.APPLICATION_JSON)
          )
          .andExpect(status().isBadRequest());
    }

    @Test
    public void 사용자_탈퇴_호스트인_팀플_존재() throws Exception {
      saveProject();

      mockMvc.perform(delete(END_POINT)
          .header("Authorization", idToken)
      ).andExpect(status().isBadRequest());
    }
  }

  private Project saveProject() {
    CreateProjectDto projectDto = new CreateProjectDto("test name", LocalDate.now(), 1L, LocalDate.now(), "test explanation");
    Project project = Project.from(projectDto, DataInitializer.testUser);

    return projectRepository.save(project);
  }
}
