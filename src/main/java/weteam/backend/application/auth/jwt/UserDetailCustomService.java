package weteam.backend.application.auth.jwt;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import weteam.backend.application.auth.PrincipalDetails;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.user.entity.User;

@AllArgsConstructor
public class UserDetailCustomService implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) {
        //        validateEmail(oAuth2Provider.getEmail());
        User user = registerUser(email);

        return new PrincipalDetails(UserDto.from(user));
    }

    private void validateEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }
    }

    private User registerUser(String email) {
        return userRepository.findByEmail(email).orElseGet(() -> userRepository.save(
                User.builder()
                    .email(email)
                    .build())
        );
    }
}
