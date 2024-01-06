package weteam.backend.application.config;

import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import weteam.backend.application.jwt.FirebaseTokenFilter;

@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final FirebaseAuth firebaseAuth;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                   .httpBasic().disable()
                   .formLogin().disable()
                   .cors().disable()
                   //                   .authorizeHttpRequests(authorize -> authorize
                   //                           .requestMatchers("/**").hasAnyRole("USER", "ADMIN")
                   //                           .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger").permitAll()
                   //                   )
                   .addFilterBefore(new FirebaseTokenFilter(userDetailsService, firebaseAuth),
                           UsernamePasswordAuthenticationFilter.class)
                   .exceptionHandling()
                   .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))

                   .and()
                   .build();
    }
}
