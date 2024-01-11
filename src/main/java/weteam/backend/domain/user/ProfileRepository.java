package weteam.backend.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.user.entity.ProfileImage;

public interface ProfileRepository extends JpaRepository<ProfileImage, Long> {
}
