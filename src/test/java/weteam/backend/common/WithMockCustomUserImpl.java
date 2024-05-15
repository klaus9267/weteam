package weteam.backend.common;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import weteam.backend.domain.user.UserRepository;
import weteam.backend.domain.user.entity.User;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class WithMockCustomUserImpl implements WithSecurityContextFactory<WithMockCustomUser> {
  private final UserRepository userRepository;

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
    final SecurityContext context = SecurityContextHolder.createEmptyContext();
    final Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(annotation.role().getKey()));
    final User user = userRepository.findByUid("hIGOWUmXSugwCftVJ2HsF9kiqfh1").orElseThrow(RuntimeException::new);
    final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
    context.setAuthentication(auth);
    return context;
  }
}
