package weteam.backend.domain.project.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import weteam.backend.common.DataInitializer;
import weteam.backend.domain.project.entity.BlackList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(DataInitializer.class)
class BlackListRepositoryTest {
  @Autowired
  BlackListRepository blackListRepository;

  @Test
  void existsByUserIdAndProjectId() {
    BlackList blackList = blackListRepository.findById(1L).orElseThrow(RuntimeException::new);
    boolean result = blackListRepository.existsByUserIdAndProjectId(blackList.getUser().getId(), blackList.getProject().getId());

    assertThat(result).isTrue();
  }
}