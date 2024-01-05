package weteam.backend.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                   .formLogin().disable()
                   .cors().disable()
                   //                   .authorizeHttpRequests(authorize -> authorize
                   //                           .requestMatchers("/**").hasAnyRole("USER", "ADMIN")
                   //                           .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger").permitAll()
                   //                   )
                   .httpBasic(withDefaults())
                   .build();
    }
}
