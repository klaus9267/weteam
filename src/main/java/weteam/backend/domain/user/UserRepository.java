package weteam.backend.domain.user;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import weteam.backend.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  @EntityGraph(attributePaths = "profileImage")
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<User> findByUid(String uid);
}
