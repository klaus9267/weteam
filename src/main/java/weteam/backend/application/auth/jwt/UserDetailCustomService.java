package weteam.backend.application.auth.jwt;

import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import weteam.backend.application.auth.PrincipalDetails;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.user.entity.User;

@AllArgsConstructor
public class UserDetailCustomService {
    private final UserRepository userRepository;


    public UserDetails loadUserByUsername(FirebaseToken token) {
        User user = registerUser(token);

        return new PrincipalDetails(UserDto.from(user));
    }

    private void validateEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }
    }

    private User registerUser(FirebaseToken token) {
        return userRepository.findByUid(token.getUid()).orElseGet(() -> userRepository.save(
                User.builder().uid(token.getUid()).email(token.getEmail()).username(token.getName()).build()));
    }
}
