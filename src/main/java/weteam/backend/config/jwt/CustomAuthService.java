package weteam.backend.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.auth.repository.AuthRepository;
import weteam.backend.auth.domain.CustomUser;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomAuthService implements UserDetailsService {
    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        return authRepository.findByUid(uid)
                             .map(CustomUser::new)
                             .orElseThrow(() -> new RuntimeException("없는 사용자"));
    }
}
