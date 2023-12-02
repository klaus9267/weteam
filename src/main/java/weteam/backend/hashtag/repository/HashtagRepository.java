package weteam.backend.hashtag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.hashtag.domain.Hashtag;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag,Long> {
    Optional<Hashtag> findByName(String name);
}
