package weteam.backend.application.config;

import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import weteam.backend.application.auth.FirebaseTokenFilter;
import weteam.backend.application.auth.jwt.UserDetailCustomService;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final UserDetailCustomService userDetailCustomService;
    private final FirebaseAuth firebaseAuth;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                   .formLogin().disable()
                   .httpBasic().disable()
                   .sessionManagement(session -> session
                           .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                   )

                   .authorizeHttpRequests(authorize -> authorize
                           .requestMatchers("/**").hasAnyRole("USER", "ADMIN")
                   )

                   .addFilterBefore(new FirebaseTokenFilter(userDetailCustomService, firebaseAuth), UsernamePasswordAuthenticationFilter.class)
                   .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/api/auths/**",
                "/favicon.ico",
                "/error",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs/**");
    }
}
