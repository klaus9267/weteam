package weteam.backend.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.application.oauth.provider.ProviderType;
import weteam.backend.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByProviderAndProviderId(ProviderType provider, String providerId);
}
