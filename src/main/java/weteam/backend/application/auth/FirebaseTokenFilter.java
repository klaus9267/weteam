package weteam.backend.application.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import weteam.backend.application.auth.user_detail.UserDetailCustomService;
import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Component
@Slf4j
public class FirebaseTokenFilter extends OncePerRequestFilter {
  private final UserDetailCustomService userDetailCustomService;

  @Override
  protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
    final String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      try {
        final String token = header.substring(7);
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

        final User user = userDetailCustomService.loadUser(decodedToken);
        final CustomUser4Log customUser = CustomUser4Log.from(user);

        log.info("---------------- login : " + customUser + " | " + request.getMethod() + "|" + request.getRequestURI() + " --------------");
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority(UserRole.USER.getKey())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

      } catch (Exception e) {
        log.error("token verification failed", e);
        log.error("--------------------");
      }
    }

    filterChain.doFilter(request, response);
  }
}
