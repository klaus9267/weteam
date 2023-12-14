package weteam.backend.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import weteam.backend.domain.auth.repository.AuthRepository;
import weteam.backend.domain.auth.domain.CustomUser;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomAuthService implements UserDetailsService {
    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        return authRepository.findByUid(uid).map(CustomUser::new).orElseThrow(() -> new NotFoundException("없는 사용자"));
    }
}
