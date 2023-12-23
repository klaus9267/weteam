package weteam.backend.application.oauth;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
@Tag(name = "oauth")
public class OAuthController {
    @GetMapping
    public Map<String, Object> tempOAuth2(@AuthenticationPrincipal OAuth2User oauth) {
        return oauth.getAttributes();
    }
}
