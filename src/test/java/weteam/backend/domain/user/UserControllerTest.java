package weteam.backend.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
class UserControllerTest {
  private final String END_POINT = "/api/users";

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("내 정보 조회")
  void readMyInfo()throws Exception {
    mockMvc.perform(get(END_POINT))
        .andDo(print());
  }
}