package weteam.backend.application.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
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
import weteam.backend.domain.user.entity.UserRole;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Component
@Slf4j
public class FirebaseTokenFilter extends OncePerRequestFilter {
  private final UserDetailCustomService userDetailCustomService;
  private final SecurityUtil securityUtil;
  private final FirebaseAuth firebaseAuth;

  @Override
  protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
    final String header = request.getHeader("Authorization");
    final String token = securityUtil.resolveToken(header);
    if (token == null) {
      filterChain.doFilter(request, response);
    } else {
      FirebaseToken decodedToken = null;
      try {
        decodedToken = firebaseAuth.verifyIdToken(token);
      } catch (FirebaseAuthException e) {
        log.error("invalid token" + request.getRemoteAddr() + " | " + request.getMethod() + " | " + request.getRequestURI());
        log.error("--------------------");
      }
      final CustomUser4Log user = userDetailCustomService.loadUser(decodedToken);
      log.info("---------------- login : " + user.toString() + " | " + request.getMethod() + "|" + request.getRequestURI() + " --------------");
      final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority(UserRole.USER.getKey())));
      SecurityContextHolder.getContext().setAuthentication(authentication);

      filterChain.doFilter(request, response);
    }
  }
}
