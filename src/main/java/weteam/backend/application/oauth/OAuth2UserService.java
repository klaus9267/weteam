package weteam.backend.application.oauth;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import weteam.backend.application.oauth.provider.OAuth2Provider;
import weteam.backend.domain.user.dto.UserDto;
import weteam.backend.domain.user.entity.User;
import weteam.backend.domain.user.entity.UserRole;
import weteam.backend.domain.user.UserRepository;

import java.util.Map;

@AllArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        final Map<String, Object> attributes = oAuth2User.getAttributes();

        final OAuth2ProviderFactory providerFactory = new OAuth2ProviderFactory();
        final OAuth2Provider oAuth2Provider = providerFactory.getOAuth2Provider(userRequest.getClientRegistration().getRegistrationId(), attributes);

        User user = userRepository.findOneByProviderAndProviderId(oAuth2Provider.getProvider(), oAuth2Provider.getProviderId()).orElseGet(() -> userRepository.save(
                User.builder()
                    .username(oAuth2Provider.getName())
                    .role(UserRole.USER)
                    .provider(oAuth2Provider.getProvider())
                    .providerId(oAuth2Provider.getProviderId())
                    .build())
        );

        return new PrincipalDetails(UserDto.from(user), attributes);
    }
}
