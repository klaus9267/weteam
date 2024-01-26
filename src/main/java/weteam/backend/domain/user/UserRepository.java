package weteam.backend.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import weteam.backend.domain.user.dto.UserWithProfileImageDto;
import weteam.backend.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUid(String uid);

    @Query("""
           SELECT new weteam.backend.domain.user.dto.UserWithProfileImageDto(u,i)
           FROM users u
                LEFT JOIN FETCH profile_images i ON u.profileImage.user = u
           WHERE u.id = :userId
           """)
    Optional<UserWithProfileImageDto> findWithProfileImage(final Long userId);
}
