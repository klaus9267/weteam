package weteam.backend.application.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import weteam.backend.application.Message;
import weteam.backend.application.auth.jwt.UserDetailCustomService;
import weteam.backend.application.handler.exception.BadRequestException;

import java.io.IOException;
import java.util.NoSuchElementException;

@AllArgsConstructor
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
            throw new BadRequestException(Message.INVALID_TOKEN);
        }

        try {
            UserDetails user = userDetailCustomService.loadUserByUsername(decodedToken);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (NoSuchElementException e) {
            throw new BadRequestException(Message.NOT_FOUND);
        }
        filterChain.doFilter(request, response);
    }
}
