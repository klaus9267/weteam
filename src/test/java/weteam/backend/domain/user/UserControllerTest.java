package weteam.backend.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.common.WithMockCustomUser;
import weteam.backend.domain.user.dto.RequestUserDto;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest extends BaseIntegrationTest {
  private final String END_POINT = "/api/users";

  @Test
  @DisplayName("내 정보 조회")
  @WithMockCustomUser
  void readMyInfo() throws Exception {
    mockMvc.perform(get(END_POINT))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("다른 사용자 정보 조회")
  @WithMockCustomUser
  void readOtherInfo() throws Exception {
    Random random = new Random();
    int n = random.nextInt(0, 100);
    mockMvc.perform(get(END_POINT + "/" + n))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(n))
        .andDo(print());
  }

  @Test
  @DisplayName("사용자 정보 변경")
  @WithMockCustomUser
  void updateUser() throws Exception {
    RequestUserDto userDto = new RequestUserDto("test", "인덕대");
    String body = mapper.writeValueAsString(userDto);

    mockMvc.perform(patch(END_POINT)
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  @DisplayName("푸시 알람 수신 변경")
  @WithMockCustomUser
  void changeReceivePermission() throws Exception {
    mockMvc.perform(get(END_POINT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.receivePermission").value(true))
        .andDo(print());

    mockMvc.perform(patch(END_POINT + "/push"))
        .andExpect(status().isNoContent());

    mockMvc.perform(get(END_POINT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.receivePermission").value(false))
        .andDo(print());
  }

  @Test
  @DisplayName("로그아웃")
  @WithMockCustomUser
  void logout() throws Exception {
    mockMvc.perform(patch(END_POINT + "/logout"))
        .andExpect(status().isNoContent())
        .andDo(print());
  }
}