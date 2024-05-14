package weteam.backend.domain.profile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.common.WithMockCustomUser;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends BaseIntegrationTest {
  private final String END_POINT = "/api/profiles";
  @Autowired
  ProfileRepository profileRepository;
  @Autowired
  UserRepository userRepository;

  @Test
  @DisplayName("프로필 사진 생성")
  @WithMockCustomUser
  void addProfileImage() throws Exception {
    mockMvc.perform(post(END_POINT + "/3"))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("프로필 사진 변경")
  @WithMockCustomUser
  void readOtherInfo() throws Exception {
    User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
    ProfileImage profileImage = new ProfileImage(null, user, 1L);
    profileRepository.save(profileImage);

    mockMvc.perform(patch(END_POINT + "/2"))
        .andExpect(status().isNoContent())
        .andDo(print());
  }
}