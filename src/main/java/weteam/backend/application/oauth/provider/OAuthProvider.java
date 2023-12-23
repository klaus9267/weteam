package weteam.backend.application.oauth.provider;

import java.util.Map;

public class OAuthProvider {
    private final ProviderType providerType;
    private final Map<String, Object> attributes;
    private final Map<String, Object> properties;

    public OAuthProvider(final Map<String, Object> attributes, final ProviderType providerType) {
        this.attributes = attributes;
        this.properties = (Map<String, Object>) attributes.get("properties");
        this.providerType = providerType;
    }

    public String getProviderId() {
        return attributes.get("id").toString();
    }

    public ProviderType getProvider() {
        return providerType;
    }

    public String getName() {
        return properties.get("nickname").toString();
    }
}
