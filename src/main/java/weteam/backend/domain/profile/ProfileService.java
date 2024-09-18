package weteam.backend.domain.profile;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.domain.profile.entity.ProfileImage;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class ProfileService {
  private final ProfileRepository profileRepository;

  @Transactional
  public void addProfile(final Long imageIdx, final User user) {
    final ProfileImage image = ProfileImage.from(imageIdx, user);
    profileRepository.save(image);
  }
}
