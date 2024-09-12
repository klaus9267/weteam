package weteam.backend.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.user.dto.RequestUserDto;
import weteam.backend.domain.user.dto.UserWithProfileImageDto;
import weteam.backend.domain.user.entity.User;

@Component
@RequiredArgsConstructor
public class UserFacade {
  private final UserService userService;

  public UserWithProfileImageDto findUserInfo(final long userId) {
    final User user = userService.findUser(userId);
    return UserWithProfileImageDto.from(user);
  }

  @Transactional
  public void toggleReceivePermission(final long userId) {
    final User user = userService.findUser(userId);
    user.toggleReceivePermission();
  }

  @Transactional
  public void updateUser(final RequestUserDto requestUserDto, final long userId) {
    final User user = userService.findUser(userId);
    user.updateUser(requestUserDto);
  }

  @Transactional
  public void deleteUser(final long userId) {
    final User user = userService.findUser(userId);
    if (!user.getProjectList().isEmpty()) {
      throw new CustomException(ErrorCode.USER_IS_HOST);
    }
    userService.deleteUser(userId);
  }

  @Transactional
  public void deleteUser4Develop(final long userId) {
    userService.deleteUser(userId);
  }

  @Transactional
  public void logOut(final long userId) {
    final User user = userService.findUser(userId);
    user.logout();
  }
}
