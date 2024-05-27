package weteam.backend.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.common.DataInitializer;
import weteam.backend.domain.user.dto.RequestUserDto;
import weteam.backend.domain.user.entity.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest extends BaseIntegrationTest {
  private final String END_POINT = "/api/users";
  @Autowired
  UserRepository userRepository;

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
          .andExpect(jsonPath("$.email").value(user.getEmail()))
          .andExpect(jsonPath("$.organization").value(user.getOrganization()))
          .andExpect(jsonPath("$.introduction").value(user.getIntroduction()))
          .andExpect(status().isOk())
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
      void updateUser() throws Exception {
        RequestUserDto userDto = new RequestUserDto("test", "인덕대", "소개");
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
            userDto.username(),
            userDto.organization(),
            userDto.introduction()
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
}