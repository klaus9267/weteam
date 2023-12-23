package weteam.backend.application.oauth.strategy;

import weteam.backend.application.oauth.provider.OAuth2Kakao;
import weteam.backend.application.oauth.provider.OAuth2Provider;

import java.util.Map;

public class KakaoProviderStrategy implements OAuth2ProviderStrategy {
    @Override
    public OAuth2Provider getProvider(Map<String, Object> attributes) {
        return new OAuth2Kakao(attributes);
    }
}