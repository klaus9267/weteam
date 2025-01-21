package weteam.backend.application.auth.user_detail;

import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;

@AllArgsConstructor
@Service
public class UserDetailCustomService {
  private final UserRepository userRepository;

  @Transactional
  @Cacheable(cacheNames = "user", key = "#uid", value = "user")
  public User loadUser(FirebaseToken token) {
    final User user = userRepository.findByUid(token.getUid()).orElseGet(() -> userRepository.save(
        User.builder()
            .uid(token.getUid())
            .email(token.getEmail())
            .username(token.getName())
            .role(UserRole.USER)
            .receivePermission(true)
            .build())
    );
    user.login();
    return user;
  }
}
