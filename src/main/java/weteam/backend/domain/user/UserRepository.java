package weteam.backend.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import weteam.backend.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //    Optional<User> findOneByProviderId(String providerId);

    Optional<User> findByUid(String uid);
}
