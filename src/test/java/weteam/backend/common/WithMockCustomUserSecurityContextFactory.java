package weteam.backend.common;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;

import java.util.Collection;
import java.util.Collections;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
    final SecurityContext context = SecurityContextHolder.createEmptyContext();
    final Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(annotation.role().getKey()));
    final User user = User.builder()
        .id(1L)
        .username(annotation.username())
        .uid("hIGOWUmXSugwCftVJ2HsF9kiqfh1")
        .role(UserRole.USER)
        .build();
    final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
    context.setAuthentication(auth);
    return context;
  }
}
