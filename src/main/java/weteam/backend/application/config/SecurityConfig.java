package weteam.backend.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors()

                   .and()
                   .httpBasic().disable()
                   .csrf().disable()
                   .formLogin().disable()
                   .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                   .and()
                   .authorizeHttpRequests(authorize -> authorize
                           .requestMatchers("/api/auth/**").permitAll()
                           .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger").permitAll()
                           .requestMatchers("/api").hasAnyRole("USER", "ADMIN")
                           .anyRequest().authenticated()
                   )
//                   .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(endPoint -> endPoint.userService(oAuth2UserService))
//                                                .defaultSuccessUrl("/oauth") // TODO: 프론트 루트 경로로 이동
//                   )
                   .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
