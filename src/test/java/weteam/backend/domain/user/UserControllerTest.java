package weteam.backend.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.common.WithMockCustomUser;
import weteam.backend.domain.user.dto.RequestUserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
}