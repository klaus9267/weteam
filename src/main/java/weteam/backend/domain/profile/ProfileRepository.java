package weteam.backend.domain.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.profile.entity.ProfileImage;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileImage, Long> {
    Optional<ProfileImage> findByUserId(final Long userId);
}
