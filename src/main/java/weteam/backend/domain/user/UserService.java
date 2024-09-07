package weteam.backend.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.project.repository.ProjectRepository;
import weteam.backend.domain.user.dto.RequestUserDto;
import weteam.backend.domain.user.dto.UserWithProfileImageDto;
import weteam.backend.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  private final SecurityUtil securityUtil;

  public UserWithProfileImageDto findById(final Long userId) {
    final User user = this.findUser(userId);
    return UserWithProfileImageDto.from(user);
  }

  private User findUser(final Long id) {
    return userRepository.findById(id).orElseThrow(CustomException.raise(ErrorCode.NOT_FOUND));
  }

  @Transactional
  public void updateUser(final RequestUserDto userDto) {
    final User user = this.findUser(securityUtil.getId());
    user.updateUser(userDto);
  }

  @Transactional
  public void updateReceivePermission() {
    final User user = this.findUser(securityUtil.getId());
    user.updateReceivePermission();
  }

  @Transactional
  public void deleteUser() {
    final Long userId = securityUtil.getId();
    if (projectRepository.existsByHostId(userId)) {
      throw new CustomException(ErrorCode.USER_IS_HOST);
    }
    userRepository.deleteById(userId);
  }

  @Transactional
  public void deleteUser(final long id) {
    userRepository.deleteById(id);
  }

  @Transactional
  public void logOut() {
    final User user = securityUtil.getCurrentUser();
    user.logout();
  }
}

