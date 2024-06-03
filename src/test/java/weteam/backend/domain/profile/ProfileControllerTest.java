package weteam.backend.domain.profile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import weteam.backend.common.BaseIntegrationTest;
import weteam.backend.common.DataInitializer;
import weteam.backend.domain.user.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends BaseIntegrationTest {
  private final String END_POINT = "/api/profiles";
  @Autowired
  ProfileRepository profileRepository;

  @Test
  @DisplayName("프로필 사진 생성")
  void addProfileImage() throws Exception {
    mockMvc.perform(post(END_POINT + "/1")
        .header("Authorization", idToken)
    ).andExpect(status().isCreated());
  }

  @Test
  @DisplayName("프로필 사진 변경")
  void readOtherInfo() throws Exception {
    User user = DataInitializer.testUser;
    ProfileImage profileImage = ProfileImage.from(1L, user);
    profileRepository.save(profileImage);

    mockMvc.perform(patch(END_POINT + "/10")
        .header("Authorization", idToken)
    ).andExpect(status().isNoContent());

    ProfileImage image = profileRepository.findByUserId(user.getId()).orElseThrow(RuntimeException::new);
    assertThat(image).extracting(
        ProfileImage::getUser,
        ProfileImage::getImageIdx
    ).containsExactly(
        user,
        10L
    );
  }
}