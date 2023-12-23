package weteam.backend.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import weteam.backend.application.oauth.OAuth2UserService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.httpBasic(withDefaults())
                   .authorizeHttpRequests(authorize -> authorize
                           .requestMatchers("/**").hasAnyRole("USER", "ADMIN")
                           .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger").permitAll()
                   )
                   .logout(logout -> logout.logoutUrl("/api/logout") // 로그아웃
                                           // .logoutSuccessUrl("/loginForm")  // TODO: 프론트 로그인 페이지 설정
                                           .invalidateHttpSession(true)  // HTTP 세션 무효화 여부
                                           .deleteCookies("JSESSIONID")  // 로그아웃 시 삭제할 쿠키 이름
                   )
                   .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(endPoint -> endPoint.userService(oAuth2UserService))
                                                .defaultSuccessUrl("/api/oauth") // TODO: 프론트 루트 경로로 이동
                   )
                   .build();
    }
}
