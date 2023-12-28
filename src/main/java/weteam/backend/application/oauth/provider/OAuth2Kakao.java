package weteam.backend.application.oauth.provider;

import java.util.Map;

public class OAuth2Kakao implements OAuth2Provider {
    private Map<String, Object> attributes; // oauth2User.getAttributes();
    private Map<String, Object> properties;

    public OAuth2Kakao(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.properties = (Map<String, Object>) attributes.get("properties");
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public ProviderType getProvider() {
        return ProviderType.KAKAO;
    }

    @Override
    public String getName() {
        return properties.get("nickname").toString();
    }
}