package weteam.backend.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@OpenAPIDefinition(
        info = @Info(title = "Weteam Swagger",
                     version = "0.1.0"))
@Configuration
public class SwaggerConfig {
//    @Bean
//    public OpenAPI openAPI(){
//
//        return new OpenAPI();
////                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
////                .security(Collections.singletonList(securityRequirement));
//    }
    @Bean
    public GroupedOpenApi openAPI() {
        String[] paths = {"/api/**"};
        return GroupedOpenApi.builder()
                             .group("DAMO API")
                             .pathsToMatch(paths)
                             .build();
    }
}
