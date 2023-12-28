package weteam.backend.application.oauth.provider;

public interface OAuth2Provider {
    String getProviderId();
    ProviderType getProvider();
    String getName();
}
