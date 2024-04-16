package weteam.backend.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import weteam.backend.application.auth.FirebaseTokenFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
  private final FirebaseTokenFilter firebaseTokenFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/auths/**",
                "/api/common/**",
                "/api/users/all",
                "/error.html",
                "/error-weteam",
                "/swagger-ui/**",
                "/v3/**"
            ).permitAll()
            .anyRequest().authenticated()
        )

        .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
