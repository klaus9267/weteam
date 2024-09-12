package weteam.backend.domain.profile;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.common.DataInitializer;
import weteam.backend.domain.profile.entity.ProfileImage;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends BaseIntegrationTest {
  private final String END_POINT = "/api/profiles";
  @Autowired
  ProfileRepository profileRepository;
  @Autowired
  UserRepository userRepository;

  @Nested
  class 성공 {
    @Test
    void 프로필_사진_생성() throws Exception {
      User user = DataInitializer.testUser;
      long imageIdx = 12L;

      mockMvc.perform(post(END_POINT + "/" + imageIdx)
          .header("Authorization", idToken)
      ).andExpect(status().isCreated());

      ProfileImage image = profileRepository.findByUserId(user.getId()).orElseThrow(NoSuchElementException::new);
      assertThat(image.getImageIdx()).isEqualTo(imageIdx);
    }

    @Test
    void 프로필_사진_변경() throws Exception {
      long imageIdx = 12L;
      User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
      final ProfileImage profileImage = ProfileImage.from(2L, user);
      profileRepository.save(profileImage);

      mockMvc.perform(patch(END_POINT + "/" + imageIdx)
          .header("Authorization", idToken)
      ).andExpect(status().isNoContent());

      ProfileImage image = profileRepository.findByUserId(user.getId()).orElseThrow(NoSuchElementException::new);
      assertThat(image.getImageIdx()).isEqualTo(imageIdx);
    }
  }
}