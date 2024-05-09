package weteam.backend.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import weteam.backend.common.DataInitializer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(DataInitializer.class)
class UserControllerTest {
  private final String END_POINT = "/api/users";

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("내 정보 조회")
  void readMyInfo() throws Exception {
    mockMvc.perform(get(END_POINT))
        .andExpect(status().isOk())
        .andDo(print());
  }
}