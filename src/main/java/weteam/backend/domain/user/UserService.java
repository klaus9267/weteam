package weteam.backend.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.application.handler.exception.ErrorCode;
import weteam.backend.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  @Cacheable(cacheNames = "user", key = "#id", value = "user")
  public User findUser(final Long id) {
    return userRepository.findById(id).orElseThrow(ErrorCode.NOT_FOUND);
  }

  @Transactional
  @CacheEvict(cacheNames = "user", key = "#id")
  public void deleteUser(final long id) {
    userRepository.deleteById(id);
  }
}

