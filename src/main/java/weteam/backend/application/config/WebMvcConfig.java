package weteam.backend.application.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns("*")    // 외부에서 들어오는 모둔 url 을 허용
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")    // 허용되는 Method
        .allowedHeaders("*")    // 허용되는 헤더
        .allowCredentials(true);    // 자격증명 허용
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/error/**")
        .addResourceLocations("classpath:/static/");
  }
}
