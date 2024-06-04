package weteam.backend.domain.profile;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

@Service
@AllArgsConstructor
public class ProfileService {
  private final ProfileRepository profileRepository;
  private final UserRepository userRepository;
  private final SecurityUtil securityUtil;
  
  @Transactional
  public void addProfile(final Long imageIdx) {
    final User user = userRepository.findById(securityUtil.getId()).orElseThrow(CustomException.raise(ErrorCode.NOT_FOUND));
    if (user.getProfileImage() != null) {
      throw new CustomException(ErrorCode.DUPLICATE);
    }
    final ProfileImage image = ProfileImage.from(imageIdx, user);
    profileRepository.save(image);
  }
  
  @Transactional
  public void updateProfile(final Long imageIdx) {
    ProfileImage image = this.findProfile();
    image.updateImage(imageIdx);
  }
  
  private ProfileImage findProfile() {
    return profileRepository.findByUserId(securityUtil.getId()).orElseThrow(CustomException.raise(ErrorCode.NOT_FOUND));
  }
}
