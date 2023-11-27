package weteam.backend.group_project.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import weteam.backend.project.domain.ProjectWork;
import weteam.backend.project.repository.ProjectRepository;
import weteam.backend.project.repository.ProjectWorkRepository;

import static org.assertj.core.util.DateUtil.now;

@SpringBootTest
class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectWorkRepository projectWorkRepository;

    @Test
    void test1() {
        ProjectWork projectWork = ProjectWork.builder()
                                             .name("sdad")
                                             .startedAt(now())
                                             .build();
        projectWorkRepository.save(projectWork);
        projectWorkRepository.findAll().forEach(System.out::println);
    }
}