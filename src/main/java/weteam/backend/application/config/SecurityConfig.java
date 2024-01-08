package weteam.backend.application.config;

import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import weteam.backend.application.auth.FirebaseTokenFilter;
import weteam.backend.application.auth.jwt.UserDetailCustomService;

@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailCustomService userDetailCustomService;
    private final FirebaseAuth firebaseAuth;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                   .httpBasic().disable()
                   .formLogin().disable()
                   .cors().disable()
                   .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                   .authorizeHttpRequests(authorize -> authorize
                                   .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger").permitAll()
                                   .requestMatchers("/**").hasAnyRole("USER", "ADMIN")
                   )
                   .addFilterBefore(new FirebaseTokenFilter(userDetailCustomService, firebaseAuth),
                           UsernamePasswordAuthenticationFilter.class)
                   .exceptionHandling()
                   .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))

                   .and()
                   .build();
    }
}
