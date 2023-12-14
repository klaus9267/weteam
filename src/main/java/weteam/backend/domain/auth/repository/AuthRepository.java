package weteam.backend.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.auth.domain.Auth;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth,Long> {
    Optional<Auth> findByUid(String uid);
}
