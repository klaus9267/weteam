package weteam.backend.application.security.jwt.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import weteam.backend.application.message.ExceptionMessage;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (JwtException ex) {
            String message = ex.getMessage();
            if (ExceptionMessage.WRONG_SIGNATURE.getMessage().equals(message)) {
                setResponse(response, ExceptionMessage.WRONG_SIGNATURE);
            } else if (ExceptionMessage.EXPIRED_TOKEN.getMessage().equals(message)) {
                setResponse(response, ExceptionMessage.EXPIRED_TOKEN);
            } else if (ExceptionMessage.UNSUPPORTED_TOKEN.getMessage().equals(message)) {
                setResponse(response, ExceptionMessage.UNSUPPORTED_TOKEN);
            } else if (ExceptionMessage.WRONG_TOKEN.getMessage().equals(message)) {
                setResponse(response, ExceptionMessage.WRONG_TOKEN);
            } else {
                setResponse(response, ExceptionMessage.ACCESS_DENIED);
            }
        }
    }

    private void setResponse(HttpServletResponse response, ExceptionMessage errorMessage) throws RuntimeException,
                                                                                                 IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorMessage.getHttpStatus());
        response.getWriter().print(errorMessage.getMessage());
    }
}
