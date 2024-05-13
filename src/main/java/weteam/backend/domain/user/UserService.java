package weteam.backend.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.auth.SecurityUtil;
import weteam.backend.application.handler.exception.CustomErrorCode;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.domain.project.repository.ProjectRepository;
import weteam.backend.domain.user.dto.RequestUserDto;
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.user.dto.UserWithProfileImageDto;
import weteam.backend.domain.user.entity.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  private final SecurityUtil securityUtil;

  public List<UserDto> findAll() {
    final List<User> userList = userRepository.findAll();
    return userList.stream().map(UserDto::from).toList();
  }

  public UserWithProfileImageDto findOneById(final Long userId) {
    final User user = this.findOne(userId);
    return UserWithProfileImageDto.from(user);
  }

  private User findOne(final Long id) {
    return userRepository.findById(id).orElseThrow(CustomException.notFound(CustomErrorCode.NOT_FOUND));
  }

  @Transactional
  public void updateUser(final RequestUserDto userDto) {
    final User user = this.findOne(securityUtil.getId());
    user.updateUser(userDto);
  }

  @Transactional
  public void updateReceivePermission() {
    final User user = this.findOne(securityUtil.getId());
    user.updateReceivePermission();
  }

  @Transactional
  public void deleteOne() {
    final Long userId = securityUtil.getId();
    if (projectRepository.existsByHostId(userId)) {
      throw new CustomException(CustomErrorCode.BAD_REQUEST, "호스트로 진행중인 팀플이 존재합니다.");
    }
    userRepository.deleteById(userId);
  }

  @Transactional
  public void logOut() {
    final User user = securityUtil.getCurrentUser();
    user.logout();
  }
}

