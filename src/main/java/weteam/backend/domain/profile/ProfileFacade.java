package weteam.backend.domain.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.user.UserService;
import weteam.backend.domain.user.entity.User;

@Component
@RequiredArgsConstructor
public class ProfileFacade {
  private final ProfileService profileService;
  private final UserService userService;

  @Transactional
  public void addProfile(final long imageIdx, final long userId) {
    final User user = userService.findUser(userId);
    if (user.getProfileImage() != null) {
      throw new CustomException(ErrorCode.DUPLICATE);
    }
    profileService.addProfile(imageIdx, user);
  }

  @Transactional
  public void updateProfile(final long imageIdx, final long userId) {
    final User user = userService.findUser(userId);
    user.updateProfileImage(imageIdx);
  }
}
