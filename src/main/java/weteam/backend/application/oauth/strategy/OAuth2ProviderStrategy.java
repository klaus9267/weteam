package weteam.backend.application.oauth.strategy;


import weteam.backend.application.oauth.provider.OAuth2Provider;

import java.util.Map;

public interface OAuth2ProviderStrategy {
    OAuth2Provider getProvider(Map<String, Object> attributes);
}
