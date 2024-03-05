package weteam.backend.application.auth.user_detail;

import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;

@AllArgsConstructor
@Service
public class UserDetailCustomService {
  private final UserRepository userRepository;
  
  @Transactional
  public UserDto loadUser(FirebaseToken token) {
    final User user = registerUser(token);
    return UserDto.from(user);
  }
  
  private User registerUser(FirebaseToken token) {
    return userRepository.findByUid(token.getUid()).orElseGet(() -> userRepository.save(
        User.builder()
            .uid(token.getUid())
            .email(token.getEmail())
            .username(token.getName())
            .role(UserRole.USER)
            .build()));
  }
}
