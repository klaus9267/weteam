package weteam.backend.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.project.entity.Project;
import weteam.backend.domain.user.dto.RequestUserDto;
import weteam.backend.domain.user.dto.UserWithProfileImageDto;
import weteam.backend.domain.user.entity.User;

@Component
@RequiredArgsConstructor
public class UserFacade {
  private final UserService userService;
  private final SecurityUtil securityUtil;

  public UserWithProfileImageDto findUserInfo(final long userId) {
    final User user = userService.findUser(userId);
    return UserWithProfileImageDto.from(user);
  }

  @Transactional
  public void toggleReceivePermission() {
    final User currentUser = this.findCurrentUser();
    currentUser.toggleReceivePermission();
  }

  public UserWithProfileImageDto findMyInfo() {
    final User user = securityUtil.getCurrentUser();
    return UserWithProfileImageDto.from(user);
  }

  private User findCurrentUser() {
    final long currentUserId = securityUtil.getCurrentUserId();
    return userService.findUser(currentUserId);
  }

  @Transactional
  public void updateUser(final RequestUserDto requestUserDto) {
    final User currentUser = this.findCurrentUser();
    currentUser.updateUser(requestUserDto);
  }

  @Transactional
  public void removeAccount() {
    final User currentUser = this.findCurrentUser();

    if (currentUser.getProjectList().isEmpty()) {
      userService.deleteUser(currentUser.getId());
      return;
    }

    for (Project project : currentUser.getProjectList()) {
      if (!project.getHost().getId().equals(currentUser.getId())) {
        throw new CustomException(ErrorCode.USER_IS_HOST);
      }
    }

    userService.deleteUser(currentUser.getId());
  }

  @Transactional
  public void deleteUser(final long userId) {
    userService.deleteUser(userId);
  }

  @Transactional
  public void logOut() {
    final User user = findCurrentUser();
    user.logout();
  }
}
