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
import org.springframework.web.filter.OncePerRequestFilter;
import weteam.backend.application.Message;
import weteam.backend.application.auth.jwt.UserDetailCustomService;
import weteam.backend.application.handler.exception.BadRequestException;
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.user.entity.UserRole;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Slf4j
public class FirebaseTokenFilter extends OncePerRequestFilter {
    private final UserDetailCustomService userDetailCustomService;
    private final FirebaseAuth firebaseAuth;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
        FirebaseToken decodedToken;
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new BadRequestException(Message.INVALID_HEADER);
        }
        String token = header.substring(7);
        try {
            decodedToken = firebaseAuth.verifyIdToken(token);
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e.getMessage());
        }

        try {
            UserDto user = userDetailCustomService.loadUser(decodedToken);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority(UserRole.USER.getKey())));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (NoSuchElementException e) {
            throw new BadRequestException(Message.NOT_FOUND);
        }
        filterChain.doFilter(request, response);
    }
}
