package weteam.backend.domain.profile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import weteam.backend.application.firebase.FirebaseUtil;
import weteam.backend.common.IntegrationTest;
import weteam.backend.domain.profile.entity.ProfileImage;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ProfileControllerTest {
  private String token;
  private final String END_POINT = "/api/profiles";
  @Autowired
  FirebaseUtil firebaseUtil;
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ProfileRepository profileRepository;

  @BeforeEach
  public void setUp() {
    token = "Bearer " + firebaseUtil.createIdToken();
  }

  @Nested
  class 성공 {
    @Test
    @DisplayName("프로필_생성")
    void createProfile() throws Exception {
      profileRepository.deleteById(1L);
      long imageIdx = 12L;

      mockMvc.perform(post(END_POINT + "/" + imageIdx)
          .header("Authorization", token)
      ).andExpect(status().isCreated());

      ProfileImage image = profileRepository.findByUserId(1L).orElseThrow(NoSuchElementException::new);
      assertThat(image.getImageIdx()).isEqualTo(imageIdx);
    }

    @Test
    @DisplayName("프로필_변경")
    void updateProfile() throws Exception {
      long imageIdx = 111L;

      mockMvc.perform(patch(END_POINT + "/" + imageIdx)
          .header("Authorization", token)
      ).andExpect(status().isNoContent());

      ProfileImage image = profileRepository.findByUserId(1L).orElseThrow(NoSuchElementException::new);
      assertThat(image.getImageIdx()).isEqualTo(imageIdx);
    }
  }
}