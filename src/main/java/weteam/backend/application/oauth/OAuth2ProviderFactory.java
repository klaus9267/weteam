package weteam.backend.application.oauth;

import weteam.backend.application.ExceptionMessage;
import weteam.backend.application.handler.exception.NotFoundException;
import weteam.backend.application.oauth.provider.OAuth2Provider;
import weteam.backend.application.oauth.provider.ProviderType;
import weteam.backend.application.oauth.strategy.GoogleProviderStrategy;
import weteam.backend.application.oauth.strategy.KakaoProviderStrategy;
import weteam.backend.application.oauth.strategy.NaverProviderStrategy;
import weteam.backend.application.oauth.strategy.OAuth2ProviderStrategy;

import java.util.HashMap;
import java.util.Map;

public class OAuth2ProviderFactory {
    private final Map<String, OAuth2ProviderStrategy> strategies = new HashMap<>();;

    public OAuth2ProviderFactory() {
        strategies.put(ProviderType.GOOGLE.getKey().toLowerCase(), new GoogleProviderStrategy());
        strategies.put(ProviderType.NAVER.getKey().toLowerCase(), new NaverProviderStrategy());
        strategies.put(ProviderType.KAKAO.getKey().toLowerCase(), new KakaoProviderStrategy());
    }

    public OAuth2Provider getOAuth2Provider(String registrationId, Map<String, Object> attributes) {
        if (!strategies.containsKey(registrationId)) {
            throw new NotFoundException(ExceptionMessage.NOT_FOUND_PROVIDER);
        }
        return strategies.get(registrationId).getProvider(attributes);
    }
}