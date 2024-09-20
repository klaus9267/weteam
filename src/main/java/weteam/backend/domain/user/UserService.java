package weteam.backend.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public User findUser(final Long id) {
    return userRepository.findById(id).orElseThrow(ErrorCode.NOT_FOUND);
  }

  @Transactional
  public void deleteUser(final long id) {
    userRepository.deleteById(id);
  }
}

