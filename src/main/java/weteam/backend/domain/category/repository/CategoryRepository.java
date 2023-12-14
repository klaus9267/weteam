package weteam.backend.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weteam.backend.domain.category.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByName(String name);

    List<Category> findAllByMemberId(Long memberId);
}
