package weteam.backend.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import kotlin.DslMarker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import weteam.backend.application.firebase.FirebaseUtil;
import weteam.backend.common.IntegrationTest;
import weteam.backend.domain.project.ProjectRepository;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.project_user.ProjectUserRepository;
import weteam.backend.domain.project_user.entity.ProjectUser;
import weteam.backend.domain.user.dto.RequestUserDto;
import weteam.backend.domain.user.entity.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class UserControllerTest {
  private final String END_POINT = "/api/users";
  @Autowired
  FirebaseUtil firebaseUtil;
  protected ObjectMapper mapper = new ObjectMapper();
  @Autowired
  MockMvc mockMvc;
  @Autowired
  UserRepository userRepository;
  @Autowired
  ProjectRepository projectRepository;
  @Autowired
  ProjectUserRepository projectUserRepository;

  private String token;

  @BeforeEach
  public void setUp() {
    token = "Bearer " + firebaseUtil.createIdToken();
  }

  @Nested
  class 성공 {
    @Test
    @DisplayName("내 정보 조회")
    void readMyInfo() throws Exception {
      User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);

      mockMvc.perform(get(END_POINT)
              .header("Authorization", token)
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
      long n = random.nextLong(2L, 10L);
      mockMvc.perform(get(END_POINT + "/" + n)
              .header("Authorization", token)
          ).andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(n));

      User user = userRepository.findById(n).orElseThrow(NoSuchElementException::new);

      assertThat(user).extracting(
          User::getUsername,
          User::getOrganization
      ).doesNotContainNull();
    }

    @Nested
    class 사용자_정보_변경 {
      @Test
      void ony_username() throws Exception {
        RequestUserDto userDto = new RequestUserDto("test", null, null);
        this.sendRequest(userDto);
      }

      @Test
      void organization_username() throws Exception {
        RequestUserDto userDto = new RequestUserDto(null, "organization", null);
        this.sendRequest(userDto);
      }

      @Test
      void introduction_username() throws Exception {
        RequestUserDto userDto = new RequestUserDto(null, null, "introduction");
        this.sendRequest(userDto);
      }

      private void sendRequest(RequestUserDto userDto) throws Exception {
        String body = mapper.writeValueAsString(userDto);

        mockMvc.perform(patch(END_POINT)
                .content(body)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        assertThat(user).extracting(
            User::getUsername,
            User::getOrganization,
            User::getIntroduction
        ).containsExactly(
            userDto.username() == null ? user.getUsername() : userDto.username(),
            userDto.organization() == null ? user.getOrganization() : userDto.organization(),
            userDto.introduction() == null ? user.getIntroduction() : userDto.introduction()
        );
      }
    }

    @Test
    @DisplayName("푸시 알람 수신 변경")
    void changeReceivePermission() throws Exception {
      mockMvc.perform(patch(END_POINT + "/push")
              .header("Authorization", token))
          .andExpect(status().isNoContent());

      User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
      assertThat(user.isReceivePermission()).isFalse();
    }

    @Test
    @DisplayName("로그아웃")
    void logout() throws Exception {
      mockMvc.perform(patch(END_POINT + "/logout")
              .header("Authorization", token))
          .andExpect(status().isNoContent());

      User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
      assertThat(user.isLogin()).isFalse();
    }

    @Test
    @DisplayName("사용자 탈퇴")
    void quit() throws Exception {
      User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);

      mockMvc.perform(delete(END_POINT)
              .header("Authorization", token))
          .andExpect(status().isNoContent());

      List<User> userList = userRepository.findAll();
      if (userList.isEmpty()) throw new NoSuchElementException("빈 배열");
      assertThat(userList).doesNotContain(user);
    }
  }

  @Nested
  class 실패 {
    @Test
    @DisplayName("다른 사용자 조회 없는 아이디")
    void noFoundUser () throws Exception {

      mockMvc.perform(get(END_POINT + "/444")
          .header("Authorization", token)
      ).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("사용자 정보 변경에 빈 값 입력")
    void updateUserIsNull() throws Exception {
      RequestUserDto userDto = new RequestUserDto(null, null, null);
      String body = mapper.writeValueAsString(userDto);

      mockMvc.perform(patch(END_POINT)
              .content(body)
              .header("Authorization", token)
              .contentType(MediaType.APPLICATION_JSON)
          )
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("호스트인 프로젝트에 팀원이 있음")
    void userIsHostAndNotEmpty() throws Exception {
      User user = userRepository.findById(2L).orElseThrow(NoSuchElementException::new);
      Project project = projectRepository.findById(1L).orElseThrow(NoSuchElementException::new);
      ProjectUser projectUser = ProjectUser.from(project, user);
      projectUserRepository.saveAndFlush(projectUser);

      mockMvc.perform(delete(END_POINT)
          .header("Authorization", token)
      ).andExpect(status().isBadRequest());
    }
  }
}
