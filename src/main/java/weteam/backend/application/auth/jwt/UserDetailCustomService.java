package weteam.backend.application.auth.jwt;

import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;

@AllArgsConstructor
@Service
public class UserDetailCustomService {
    private final UserRepository userRepository;

    public UserDto loadUser(FirebaseToken token) {
        User user = registerUser(token);
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
